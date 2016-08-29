package testing


import exceptions.PhotosException
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
@Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])  //
//@Secured(['permitAll'])
class PhotosController {

    def userService
    def photosService

    def index(PhotosBean bean) {
        bean.user = userService.currentUser
        println "-- ${bean.user }"
        bean.listPhotos(bean.user)
        render (view: 'create', model:[instance:bean])
    }

    def listPhotos(PhotosBean bean) {
        bean.user = userService.currentUser
        bean.listPhotos(bean.user)
        render (template: 'loadImages', model:[instance:bean])
    }

    def delPhoto() {
        Photos ph = Photos.get(params.id as Long)
        if (ph) {
            def user = userService.currentUser
            if (user == ph.user) {
                photosService.delPhoto(ph)
                render status:response.SC_OK
                return
            }
        }
        render status:response.SC_NOT_FOUND
    }

    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])
    def uploadPhotoJson(PhotosBean bean) {
        bean.requestType='JSON'
        uploadPhotos(bean,request,params)
    }


    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])
    def uploadPhoto(PhotosBean bean) {
        uploadPhotos(bean,request,params)
    }

    private def uploadPhotos(PhotosBean bean, request, params) {
        bean.user = userService.currentUser
        def file=request.getFile("imageFile")
        if (file) {
            bean.formatRequest(file)
        }  else {
            if (params.imagebase64) {
                bean.formatRequest(params.imagebase64)
            }
        }
        bean.validate()
        try {
            if (!bean.hasErrors()) {
                Photos check = Photos.findByImageSHaAndUser(bean.imageSHa,bean.user)
                if (check||bean.id) {
                    photosService.update((check ? check.id :  bean.id as Long) , bean)
                } else {
                    photosService.create(bean)
                }
                if (bean.requestType=='JSON') {
                    def aa = [name:bean.fileName, error: 'success', image:bean.image, contentType:bean.contentType] as JSON
                    render (aa as String)
                } else {
                    redirect(action: 'index')
                }
                return
            }
        } catch (Throwable|PhotosException t) {
            println "-- need to render ${t} vs ${bean.errors}"
        }
        if (bean.requestType=='JSON') {
            def error=bean.errors.allErrors.collect{g.message(error : it)}
            def respond = [name:bean.fileName, error: error ? error : 'Upload error occured',image:'', contentType:''] as JSON
            render (respond as String)
        } else {
            render(view: 'create', model: [instance: bean])
        }
        return
    }
    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])
    def makeDefault(String id) {
        Photos ph = Photos.get(id as Long)
        if (ph) {
            def user = userService.currentUser
            if (user == ph.user) {
                user.attributes.profilePhoto=ph
                render status:response.SC_OK
                return
            }
        }
        render status:response.SC_NOT_FOUND
    }



    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])
    def caption() {
        println "--in caption"
        def id = params.id as Long
        if (id) {
            User user=userService.currentUser
            //Photos p = user?.attributes?.profilePhoto?.get(id)
            Photos p = Photos.get(id)
            if (p) {
                println "-- user ${user } has ${p}"
                def result=PhotosBean.displayPhoto(p,500,500)
                render (template: '/photos/caption', model:[user:user, photo:result])
                return
            }
        }
        render status:response.SC_NOT_FOUND
    }
    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_FACEBOOK'])
    def saveCaption() {
        def id = params.id as Long
        if (id) {
            User user = userService.currentUser
            Photos p = user?.attributes?.profilePhoto?.get(id)
            if (p) {
                p.caption = params?.caption?.trim()
                p.save(flush: true)
            }
            render status:response.SC_OK
        }
    }


    @Secured(['ROLE_ADMIN'])
    def show() {
        def model= loadForEdit(formatBean(params.long('id')))
        return model
    }
    @Secured(['ROLE_ADMIN'])
    def copy() {
        def model=loadForEdit(formatBean(params.long('id')))
        model.instance.id=null
        model.copyId=params.id
        return model
    }
    @Secured(['ROLE_ADMIN'])
    def edit() {
        return loadForEdit(formatBean(params.long('id')))
    }

    private def formatBean(id) {
        def bean = Photos.read(id)
        if (!bean) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'photos.title.label'),id])
            return null
        }
        def bean1=new PhotosBean().formatInput(bean)
        return bean1
    }

    private def loadForEdit(bean) {
        if (bean==null) {
            render status:200,template:'/errors'
            return
        }
        def model= [instance:bean]
        return model
    }

    private def rebuildBean() {
        def bean=new PhotosBean()
        bindData(bean,params)
        bean.validate()
        return bean
    }
}
