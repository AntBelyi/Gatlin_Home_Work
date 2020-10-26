package demoanton

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("https://applegadjet.com.ua")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "application/font-woff2;q=1.0,application/font-woff;q=0.9,*/*;q=0.8",
		"Accept-Encoding" -> "identity")

	val headers_4 = Map(
		"Origin" -> "https://applegadjet.com.ua",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/x-www-form-urlencoded; charset=UTF-8",
		"Origin" -> "https://applegadjet.com.ua",
		"X-Requested-With" -> "XMLHttpRequest")



	val scn = scenario("RecordedSimulation")
		.exec(http("homePage")
			.get("/")
			.check(status.is(200))
			.check(regex("<title>(.+?)</title>").is(" AppleGadjet"))
			.headers(headers_0))
		.pause(1, 5)
		// first
		.exec(http("iPhonePage")
			.get("/?search=iPhone")
			.check(status.is(200))
			.headers(headers_0))
		.pause(1, 5)
		// apple
		.exec(http("selectedIPhonePage")
			.get("/product/apple-iphone-6s-16-gb-spacegray")
			.check(status.is(200))
			.check(regex("<title>(.+?)</title>").is("Apple iPhone 6s 16 GB SpaceGray  -  AppleGadjet"))
		.headers(headers_0))
		.pause(9)
		// buy
		.exec(http("buyAndGoToCart")
			.post("/cart")
			.check(status.is(500))
			.headers(headers_4)
			.formParam("_token", "QvxdkfqH0p0C8AotiwQ26UEblElyvVjQOjremx4g")
			.formParam("product", "1"))
		.pause(12)
		// main page
		.exec(http("homePageAgain")
			.get("/")
			.check(status.is(200))
			.check(regex("<title>(.+?)</title>").is(" AppleGadjet"))
		.headers(headers_0))
		.pause(9)
		// watch
		.exec(http("watchPage")
			.get("/?search=Watch")
			.check(status.is(200))
			.check(regex("<title>(.+?)</title>").is("Поиск по сайту -  AppleGadjet")))
		.pause(15)
		// product
		.exec(http("selectedWatchPage")
			.get("/product/apple-watch-42mm-space-gray-alluminum-case-black-sport-band-mj3t2")
			.check(status.is(200))
			.check(regex("<title>(.+?)</title>").is("Apple Watch 42mm Space Gray Alluminum Case Black Sport Band (MJ3T2) -  AppleGadjet"))
		.headers(headers_0))
		.pause(7)
		// buy
		.exec(http("buyAndGoToCartFromWatch")
			.post("/cart")
			.check(status.is(500))
			.headers(headers_4)
			.formParam("_token", "QvxdkfqH0p0C8AotiwQ26UEblElyvVjQOjremx4g")
			.formParam("product", "31")
			.formParam("addition[1]", "1035"))

	setUp(scn.inject(rampUsers(4) during (15 seconds))).assertions(
		global.successfulRequests.percent.gt(95)
	).protocols(httpProtocol)
}