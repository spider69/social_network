package benchmark

import benchmark.UsersGenerator.generateUsers
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.database.MySqlProvider
import org.scalatest.funsuite.AnyFunSuiteLike
import slick.jdbc.MySQLProfile

class TestDataGenerator
  extends AnyFunSuiteLike
  with UsersGenerator[MySQLProfile]
  with LazyLogging
{

  override val databaseProvider = new MySqlProvider

  val batchSize = 20000

  test("generate users") {
    logger.info("Generating users:")
    val users = generateUsers(1000000)
    logger.info("Inserting users:")
    var counter = 0
    users.grouped(batchSize).foreach { usersBatch =>
      addUsers(usersBatch)
      counter += batchSize
      logger.info(s"Inserted: $counter")
    }
  }

}
