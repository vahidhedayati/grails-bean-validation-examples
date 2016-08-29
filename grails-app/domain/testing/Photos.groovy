package testing
//@Resource(uri = '/profilePhotoUpload', formats = ['json','xml'])
class Photos {

    public static final int MAX=5
    public static final byte ACTIVE=0
    public static final byte OFFLINE=1
    public static final byte DELETED=2
    public static final List PHOTO_STATUSES=[ACTIVE,OFFLINE,DELETED]
   // static belongsTo = [userAttributes: UserAttributes]
    User user
    String imageSHa
    String contentType
    String fileExtension
    String caption
    byte position
    byte status=0
    boolean main=false

    static constraints = {
        user(nullable: false)
        imageSHa(nullable: false, unique: ['user'])//, validator: PhotosBean.checkBase64)
        position(nullable:true, min:(byte)0,max:(byte) 10)
        caption(nullable:true, maxSize:255)
        status(nullable:false, inList: PHOTO_STATUSES)
        main(nullable:false)
    }

    static mapping = {
        position(sqlType: 'tinyint(2)')
        main(sqlType: 'bit(1)', defaultValue: ACTIVE)
        status(sqlType: 'tinyint(1)')
    }

}
