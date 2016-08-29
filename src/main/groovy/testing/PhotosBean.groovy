package testing


import grails.util.Holders
import grails.validation.Validateable
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.imgscalr.Scalr
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.FileCopyUtils
import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class PhotosBean implements Validateable{

    //VERY IMPORTANT : Where should the images be stored on the application servers file system
    //for Unix it can be /opt/someSite for windows it will probably be something like
    //C:\\\\folder_name\\\\someSite
    static String ROOT_PATH="/opt/site"  //ServletContextHolder.servletContext.getRealPath('/')

    public final static byte NEW=0
    public final static byte VIEWED=1
    public final static byte DELETED=2
    public final static List STATUSES=[NEW,VIEWED,DELETED]



    String rootPath=ROOT_PATH
    public final static int MIN_IMAGE_WIDTH = 100
    public final static int MIN_IMAGE_HEIGHT = MIN_IMAGE_WIDTH
    public final static int MAX_IMAGE_WIDTH = 10000
    public final static int MAX_IMAGE_HEIGHT = MAX_IMAGE_WIDTH
    public final static int PROFILE_PHOTO_LIMIT = 10  //Limit user to maximum of 5 photos
   // def userService = Holders.grailsApplication.mainContext.getBean('userService')

    def id
    String image
    String imageSHa
    String caption
    byte position
    boolean main=false
    String scaledImage
    User user
    String contentType  //image/gif
    String fileName
    String requestType='html'

    //The functionality to format then defines the rest of this bean accordingly
    // Either this
    MultipartFile imageFile
    //Or this
    String imagebase64  //base64 string provided by browser

    int fileSize
    String fileExtension
    int imageHeight
    int imageWidth
    BufferedImage imageBuffer
    BufferedImage scaledImg     //scaled for presentation
    FileInputStream imageStream

    List userImages=[]

    byte status=Photos.ACTIVE

    static constraints = {
        id(nullable:true,bindable:true)
        imageSHa(nullable:false, unique:'user')
        image(nullable:true,validator:checkBase64) //, matches:/^([A-Za-z0-9+\/]{4})*([A-Za-z0-9+\/]{4}|[A-Za-z0-9+\/]{3}=|[A-Za-z0-9+\/]{2}==)\u0024/) unique:'user',nullable:true,
        imagebase64(nullable:true)
        contentType(nullable:true)
        fileName(nullable:true)
        fileSize(nullable:true)
        scaledImage(nullable:true)
        scaledImg(nullable:true)
        fileExtension(nullable:true)
        imageHeight(nullable:false, validator: checkImageHeight)
        imageWidth(nullable:false, validator: checkImageWidth)
        caption(nullable:true)
        position(nullable:true)
    }

    boolean getJsonRequest() {
        return requestType=='JSON'
    }
    //This little trickster will attempt to bind to existing photo's id of a user
    //if user has maxed their limit
    //this way the same image id gets reused on the db over and over again
    protected def newOrExistingFile() {
        //int allphotos = user?.attributes?.photos?.size()
        def allphotos = Photos.findAllByUser(user)
        if (allphotos?.size()==PhotosBean.PROFILE_PHOTO_LIMIT) {
            //id=user?.attributes?.photos?.find{it.status==Photos.DELETED}.id
            id = allphotos.find{it.status==Photos.DELETED}?.id
        }
    }
    //Format by file if request had a file this is the way things are formatted
    protected def formatRequest(MultipartFile file) {
        imageFile=file
        imageStream=imageFile.getInputStream()
        imageBuffer=ImageIO.read(imageStream)
        contentType=imageFile.contentType
        fileSize=imageFile.size
        fileName=imageFile.originalFilename
        fileExtension=fileName.contains('.') ? fileName.substring(fileName.lastIndexOf('.'),fileName.length()) : 'jpg'
        contentType=' image/'+fileExtension
        imageWidth=(imageBuffer.width as int)
        imageHeight=(imageBuffer.height as int)
        image=FileCopyUtils.copyToByteArray(imageFile.getInputStream()).encodeBase64().toString()
        imageSHa=DigestUtils.shaHex(image)
        File userFile = new File(rootPath + '/' + user.username)
        userFile.mkdirs()
        file.transferTo(new File(rootPath + '/' + user.username + '/' + imageSHa))
        newOrExistingFile()
        return this
    }
    protected def listPhotos(User user) {
        user?.attributes?.photos?.each { ph ->
            println "-- we have ${ph}"
                this.formatInput(ph)
                this.userImages << [image: image, scaledImage: scaledImage, caption:caption, contentType: contentType, id: id]

        }
    }
    // More long winded way of doing same as above
    // in the cases of where only base64 string of file was sent
    protected def formatRequest(String inputbase64) {
        if (inputbase64.startsWith('data:')) {
            def imageSplit=inputbase64?.split(',') //data:image/gif;base64
            def imageFirst=imageSplit[0]
            contentType=(imageFirst.split(':')[1]).split(';')[0]  //image/gif
            fileExtension=(imageFirst.split('/')[1]).split(';')[0] //gif
            image = imageSplit[1] //the rest of the string not shown but raw base64 string
        } else {
            image = inputbase64  //raw string we have no information about file though
            contentType='image/jpg'
            fileExtension='jpg'
        }
        if (Base64.isBase64(image)) {
            imageSHa = DigestUtils.shaHex(image)
            byte[] data = Base64.decodeBase64(image)
            File userFile = new File(rootPath + "/" + user.username)
            userFile.mkdirs()
            OutputStream stream = new FileOutputStream(rootPath + '/' + user.username + '/' + imageSHa)
            stream.write(data)
            fileName = imageSHa
            Path path = Paths.get(rootPath + '/' + user.username + '/' + imageSHa);
            String name = fileName
            String originalFileName = fileName+'.'+fileExtension
            String contentType = contentType
            byte[] content = null
            try {
                content = Files.readAllBytes(path);
            } catch (final IOException e) {
            }
            MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content)
            InputStream inputStream = new FileInputStream(rootPath + '/' + user.username + '/' + imageSHa);
            imageFile =  result
            imagebase64=inputbase64
            imageStream =  inputStream
            imageBuffer = ImageIO.read(imageStream)
            contentType = imageFile.contentType
            fileSize = imageFile.size
            fileName = imageFile.originalFilename
            imageWidth = (imageBuffer.width as int)
            imageHeight = (imageBuffer.height as int)
            newOrExistingFile()
        }
        return this
    }

    // This formats bean for presenation and produces a thumbnail of actual image
    // all outputs are base64 of image and thumbnail..
    protected def formatInput(Photos photo) {
        id=photo.id
        imageSHa=photo.imageSHa
        contentType=photo.contentType
        fileExtension=photo.fileExtension
        //remove . from fileExtension
        def noDotExtension=fileExtension.substring(1)
        user=photo.user
        File f = new File(ROOT_PATH + '/' + user.username + '/' + imageSHa)
        if(f.exists() && !f.isDirectory()) {
            imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
            image = FileCopyUtils.copyToByteArray(imageStream).encodeBase64().toString()
            caption = photo.caption
            position = photo.position
            //Lets present the image as a thumbNail
            imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
            imageBuffer = ImageIO.read(imageStream)
            scaledImg = Scalr.resize(imageBuffer, Scalr.Method.QUALITY, 150, 100, Scalr.OP_ANTIALIAS)
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(scaledImg, noDotExtension, os)
            InputStream is = new ByteArrayInputStream(os.toByteArray())
            scaledImage = FileCopyUtils.copyToByteArray(is).encodeBase64().toString()

        }
        return this
    }

    protected def moveFile(Photos photo) {
        id=photo.id
        imageSHa=photo.imageSHa
        contentType=photo.contentType
        fileExtension=photo.fileExtension
        //remove . from fileExtension
        def noDotExtension=fileExtension.substring(1)
        user=photo.user
        String path=ROOT_PATH + '/' + user.username + '/'
        File f = new File(path + imageSHa)
        if (f) {
            Helper.deleteFile(path,path+'delete/',imageSHa)
        }

    }



    static Map getPhoto(Photos photo, int width=100, int height=100) {
        File f
        def contentType
        if (photo.status==Photos.ACTIVE) {
            def id = photo.id
            def imageSHa = photo.imageSHa
            contentType = photo.contentType
            def fileExtension = photo.fileExtension
            //remove . from fileExtension
            def noDotExtension = fileExtension.substring(1)
            def user = photo.user
            f = new File(ROOT_PATH + '/' + user.username + '/' + imageSHa);
            if (f.exists() && !f.isDirectory()) {
                f = new File(ROOT_PATH + '/' + user.username + '/' + imageSHa+'_email');
                if (!f.exists()) {
                    def imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
                    def image = FileCopyUtils.copyToByteArray(imageStream).encodeBase64().toString()
                    def caption = photo.caption
                    def position = photo.position
                    // String caption=photo.caption
                    //Lets present the image as a thumbNail
                    imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
                    def imageBuffer = ImageIO.read(imageStream)
                    def scaledImg = Scalr.resize(imageBuffer, Scalr.Method.QUALITY, width, height, Scalr.OP_ANTIALIAS)
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(scaledImg, noDotExtension, os)
                    InputStream is = new ByteArrayInputStream(os.toByteArray())

                    def scaledImage = FileCopyUtils.copyToByteArray(is).encodeBase64().toString()
                    //imageSHa = DigestUtils.shaHex(scaledImg)
                    byte[] data = Base64.decodeBase64(scaledImage)
                    OutputStream stream = new FileOutputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa+'_email')
                    stream.write(data)
                    f = new File(ROOT_PATH + '/' + user.username + '/' + imageSHa+'_email');
                }
                return [file:f, contentType:'img/'+fileExtension.substring(1)]
            }

        }
        return [:]
    }
    static Map displayPhoto(Photos photo, int width=100, int height=100) {
        if (photo.status==Photos.ACTIVE) {
            def id = photo.id
            def imageSHa = photo.imageSHa
            def contentType = photo.contentType
            def fileExtension = photo.fileExtension
            //remove . from fileExtension
            def noDotExtension = fileExtension.substring(1)
            def user = photo.user
            File f = new File(ROOT_PATH + '/' + user.username + '/' + imageSHa);
            if (f.exists() && !f.isDirectory()) {

                def imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
                def image = FileCopyUtils.copyToByteArray(imageStream).encodeBase64().toString()
                def caption = photo.caption
                def position = photo.position
                // String caption=photo.caption
                //Lets present the image as a thumbNail
                imageStream = new FileInputStream(ROOT_PATH + '/' + user.username + '/' + imageSHa)
                def imageBuffer = ImageIO.read(imageStream)
                def scaledImg = Scalr.resize(imageBuffer, Scalr.Method.QUALITY, width, height, Scalr.OP_ANTIALIAS)
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(scaledImg, noDotExtension, os)
                InputStream is = new ByteArrayInputStream(os.toByteArray())
                def scaledImage = FileCopyUtils.copyToByteArray(is).encodeBase64().toString()

                return [scaledImage: scaledImage, imageSHa: imageSHa, fileExtension: fileExtension, contentType: contentType, caption: caption, id: id, image: image]
            }
            return [scaledImage: null, imageSHa: imageSHa, fileExtension: fileExtension, contentType: contentType, caption: null, id: id, image: null]
        }
        return [:]
    }

    static def checkImageWidth= { val, obj, errors ->
        if (obj.imageHeight) {
          if (obj.imageHeight  < MIN_IMAGE_WIDTH) {
              errors.rejectValue(propertyName, "image.width.too.small", [''] as Object[], '')
          }
          if (obj.imageHeight  > MAX_IMAGE_WIDTH) {
              errors.rejectValue(propertyName, "image.width.too.large", [''] as Object[], '')
          }
        }
    }

    static def checkImageHeight= { val, obj, errors ->
        if (obj.imageHeight) {
            if (obj.imageHeight  < MIN_IMAGE_HEIGHT) {
                errors.rejectValue(propertyName, "image.height.too.small", [''] as Object[], '')
            }
            if (obj.imageHeight  > MAX_IMAGE_HEIGHT) {
                errors.rejectValue(propertyName, "image.height.too.large", [''] as Object[], '')
            }
        }
    }

    static def checkBase64={val,obj,errors->
        //if (obj?.user.attributes.photos.
        //.findAll{it.status==Photos.ACTIVE}?
        if (obj?.user?.attributes?.photos?.size() >= PROFILE_PHOTO_LIMIT) {
            errors.rejectValue(propertyName,'exceeds.photo.limit',[''] as Object[],'')
            return
        }
        if (val) {
            if (!Base64.isBase64(val)) {
                errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
                return
            }
        } else {
            if (!obj.imagebase64) {
                errors.rejectValue(propertyName, "notdefined.$propertyName", [''] as Object[], '')
            }
        }
        return
    }

}
