package de.debuglevel.greeting.rest.greeting

import de.debuglevel.greeting.rest.responsetransformer.JsonTransformer
import mu.KotlinLogging
import spark.kotlin.RouteHandler

object GreetingController {
    private val logger = KotlinLogging.logger {}

    fun getOne(): RouteHandler.() -> String {
        return {
            val name = params(":name")
            logger.debug("Got GET request on '/greetings/$name'")

            try {
                val greeting = Greeter.greet(name)

                type(contentType = "application/json")
                JsonTransformer.render(greeting)
            } catch (e: Greeter.GreetingException) {
                logger.info("Name '$name' could not be greeted: ", e.message)
                response.type("application/json")
                response.status(400)
                "{\"message\":\"name '$name' could not be greeted: ${e.message}\"}"
            }
        }
    }

//    fun getOneHtml(): RouteHandler.() -> String {
//        return {
//            val dinnerId = request.params(":dinnerId").toInt()
//            logger.debug("Got GET request on '/dinners/$dinnerId'")
//
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "dinner/show.html.mustache"))
//        }
//    }

    fun getList(): RouteHandler.() -> String {
        return {
            logger.debug("Got GET request on '/greetings/'")

            val dinners = setOf<GreetingDTO>(
                    GreetingDTO("Mozart"),
                    GreetingDTO("Beethoven"),
                    GreetingDTO("Haydn")
            )

            type(contentType = "application/json")
            JsonTransformer.render(dinners)
        }
    }

//    fun getListHtml(): RouteHandler.() -> String {
//        return {
//            logger.debug("Got GET request on '/dinners/'")
//
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "dinner/list.html.mustache"))
//        }
//    }

//    fun getAddFormHtml(): RouteHandler.() -> String {
//        return {
//            logger.debug("Got GET request on '/participants'")
//
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "participant/add.html.mustache"))
//        }
//    }
}