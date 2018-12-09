package de.debuglevel.greeting.rest.greeting

import de.debuglevel.greeting.rest.responsetransformer.JsonTransformer
import mu.KotlinLogging
import spark.kotlin.RouteHandler

object GreetingController {
    private val logger = KotlinLogging.logger {}

    fun getOne(): RouteHandler.() -> String {
        return {
            val name = params(":name")

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
//            val greetingId = request.params(":greetingId").toInt()
//
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "greeting/show.html.mustache"))
//        }
//    }

    fun getList(): RouteHandler.() -> String {
        return {
            val greetings = setOf<GreetingDTO>(
                    GreetingDTO("Mozart"),
                    GreetingDTO("Beethoven"),
                    GreetingDTO("Haydn")
            )

            type(contentType = "application/json")
            JsonTransformer.render(greetings)
        }
    }

//    fun getListHtml(): RouteHandler.() -> String {
//        return {
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "greeting/list.html.mustache"))
//        }
//    }

//    fun getAddFormHtml(): RouteHandler.() -> String {
//        return {
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "greeting/add.html.mustache"))
//        }
//    }
}