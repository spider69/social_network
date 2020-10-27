package benchmark

import java.util.UUID

import com.yusupov.social_network.database.DatabaseProvider
import com.yusupov.social_network.utils.SecurityUtils
import slick.jdbc.JdbcProfile

import scala.util.Random

object UsersGenerator {

  val random = new Random()

  case class User(
    id: String,
    passwordHash: String,
    salt: Array[Byte],
    firstName: Option[String],
    lastName: Option[String],
    age: Option[Int],
    gender: Option[String],
    interests: Option[String],
    city: Option[String]
  )

  def generateUsers(num: Int) = Seq.fill[User](num)(generateUser)

  def generateUser: User = {
    val id = generateId
    val (passwordHash, salt) = generatePassword
    val firstName = generateName(firstNames)
    val lastName = generateName(lastNames)
    val age = random.nextInt(99) + 1
    val gender = generateName(genders)
    val interests = "Books, Music, Walking"
    val city = generateName(cities)
    User(id, passwordHash, salt, Some(firstName), Some(lastName), Some(age), Some(gender), Some(interests), Some(city))
  }

  val firstNames = Seq(
    "Oliver", "Jack", "Harry", "Jacob", "Charlie",
    "Thomas", "George", "Oscar", "James", "William",
    "Amelia", "Olivia", "Isla", "Emily", "Poppy",
    "Ava", "Isabella", "Jessica", "Lily", "Sophie"
  )

  val lastNames = Seq(
    "Smith", "Jones", "Williams", "Brown", "Taylor",
    "Davies", "Wilson", "Evans", "Thomas", "Roberts",
    "Murphy", "O'Kelly", "O'Sullivan", "Byrne", "O'Connor",
    "Garcia", "Rodriguez", "Wilson", "Johnson", "White"
  )

  val genders = Seq(
    "M" , "F"
  )

  val cities = Seq(
    "Bangkok", "Paris", "London", "Dubai", "Singapore",
    "Kuala Lumpur", "New York City", "Istanbul", "Tokyo", "Antalya"
  )

  def generateId = {
    val id = UUID.randomUUID().toString
    val domain = UUID.randomUUID().toString
    s"$id@$domain.com"
  }

  def generatePassword = {
    val password = UUID.randomUUID().toString
    val salt = SecurityUtils.generateSalt()
    val passwordHash = SecurityUtils.passwordHash(password, salt)
    (passwordHash, salt)
  }

  def generateName(names: Seq[String]) = {
    val randomId = random.nextInt(names.size)
    names(randomId)
  }

}

import UsersGenerator._

trait UsersGenerator[A <: JdbcProfile] {

  val databaseProvider: DatabaseProvider[A]

  import databaseProvider.profile.api._

  class UsersTable(tag: Tag) extends Table[User](tag, "Users") {
    def id = column[String]("id", O.PrimaryKey)
    def passwordHash = column[String]("password_hash")
    def salt = column[Array[Byte]]("salt", O.SqlType("binary"), O.Length(64))

    def firstName = column[Option[String]]("first_name")
    def lastName = column[Option[String]]("last_name")
    def age = column[Option[Int]]("age")
    def gender = column[Option[String]]("gender")
    def interests= column[Option[String]]("interests")
    def city= column[Option[String]]("city")

    def * = (id, passwordHash, salt, firstName, lastName, age, gender, interests, city).mapTo[User]
  }

  val table = TableQuery[UsersTable]

  def addUsers(users: Seq[User]) = databaseProvider.exec(table ++= users)

}
