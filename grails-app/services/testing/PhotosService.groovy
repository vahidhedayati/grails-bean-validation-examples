package testing

import grails.transaction.Transactional
import grails.validation.ValidationException

@Transactional
class PhotosService {

    static transactional=true

    def userService


    Photos create(PhotosBean values) {
        return save(new Photos(),values)
    }

    Photos update(Long id,PhotosBean values) {
        def photo=Photos.get(id)
        assert photo,"Photo record $id not found"
        return save(photo,values)
    }

    private Photos save(Photos photo,PhotosBean values) throws ValidationException {
        photo.with{
            imageSHa=values.imageSHa
            user=values.user
            contentType=values.contentType
            fileExtension=values.fileExtension
            status=Photos.ACTIVE
        }
        // userService.getCurrentUser()//
        if (!photo.validate()) {
            throw new ValidationException('',photo.errors)
            //photo.errors.allErrors.each {err ->
            //   println "[$err.field]: $err"
            //}
        }
        if (photo.save(flush:true)) {
            photo.user.attributes.addToPhotos(photo)
            /*
            def check = verifyPhoto( photo, values.user.attributes.photos)
            if (check.found) {
                values.user.attributes.photos=check.listing
                if (!values.user.attributes.profilePhoto|| ( photo.user.attributes.profilePhoto && photo.user.attributes.profilePhoto.status!=Photos.ACTIVE)) {
                    values.user.attributes.profilePhoto = photo
                }

                values.user.save()

            }
            */
        }
        return photo
    }


}