package testing

import java.time.LocalDate
import java.time.temporal.ChronoUnit


class UserAttributes  {

	static final int MIN_AGE=18

	static final String MAN='MA'
	static final String WOMAN='WM'

	static hasMany=[photos:Photos]
	static belongsTo=User

	Date dob
	Photos profilePhoto
	String gender=MAN
	String email
	def age
	static transients = ['age', 'zodiacSign']

	static constraints= {
		photos(nullable:true) ///, validator: checkPhotos)
		profilePhoto(nullable:true)
		email(nullable:false)
	}


	static def checkPhotos= { val, obj, errors ->
		if (val && ( obj.photos && obj.photos.findAll{}.size() > Photos.MAX)) {
			errors.rejectValue(propertyName, "exceed.photos.limit", [''] as Object[], '')
		}
	}

	def getAge() {
		if (dob) {
			LocalDate today = LocalDate.now()
			LocalDate BirthDay = new java.sql.Date(dob.getTime()).toLocalDate()
			age = ChronoUnit.YEARS.between(BirthDay, today)
		} else {
			age=18
		}
	}

	def getZodiacSign() {
		return Helper.zodiacSign(dob.month,dob.day)
	}
}
