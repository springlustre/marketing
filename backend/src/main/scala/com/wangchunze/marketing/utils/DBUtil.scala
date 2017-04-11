package com.wangchunze.marketing.utils


//import slick.driver.MySQLDriver.api._
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import slick.jdbc.MySQLProfile.api._
import com.wangchunze.marketing.common.AppSettings._
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
/**
  * User: Taoz
  * Date: 2/9/2015
  * Time: 4:33 PM
  */
object DBUtil {
  val log = LoggerFactory.getLogger(this.getClass)

  private val dataSource = createDataSource()

  private def createDataSource() = {

//    val dataSource = new org.postgresql.ds.PGSimpleDataSource()

    val dataSource = new MysqlDataSource()

    log.info(s"connect to db: $slickUrl")
    dataSource.setUrl(slickUrl)
    dataSource.setUser(slickUser)
    dataSource.setPassword(slickPassword)

    val hikariDS = new HikariDataSource()
    hikariDS.setDataSource(dataSource)
    hikariDS.setMaximumPoolSize(slickMaximumPoolSize)
    hikariDS.setConnectionTimeout(slickConnectTimeout)
    hikariDS.setIdleTimeout(slickIdleTimeout)
    hikariDS.setMaxLifetime(slickMaxLifetime)
    hikariDS.setAutoCommit(true)
    hikariDS
  }
  val db = Database.forDataSource(dataSource, Some(slickMaximumPoolSize))

}