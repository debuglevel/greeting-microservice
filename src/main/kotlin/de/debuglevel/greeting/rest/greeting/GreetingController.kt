package de.debuglevel.greeting.rest.greeting

import de.debuglevel.greeting.domain.greeting.GreetingService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import mu.KotlinLogging

@Controller("/greetings")
class GreetingController(val greetingService: GreetingService) {
    private val logger = KotlinLogging.logger {}

    @Get("/{name}")
    fun getOne(name: String): GreetingDTO {
        return greetingService.greet(name)
    }

//    fun getOne(): RouteHandler.() -> String {
//        return {
//            val name = params(":name")
//
//            try {
//                val greeting = Greeter.greet(name)
//
//                type(contentType = "application/json")
//                JsonTransformer.render(greeting)
//            } catch (e: Greeter.GreetingException) {
//                logger.info("Name '$name' could not be greeted: ", e.message)
//                response.type("application/json")
//                response.status(400)
//                "{\"message\":\"name '$name' could not be greeted: ${e.message}\"}"
//            }
//        }
//    }

//    fun getOneHtml(): RouteHandler.() -> String {
//        return {
//            val greetingId = request.params(":greetingId").toInt()
//
//            val model = HashMap<String, Any>()
//            MustacheTemplateEngine().render(ModelAndView(model, "greeting/show.html.mustache"))
//        }
//    }

    @Get("/")
    fun getList(): Set<GreetingDTO> {
        val greetings = setOf<GreetingDTO>(
            GreetingDTO("Mozart"),
            GreetingDTO("Beethoven"),
            GreetingDTO("Haydn")
        )

        return greetings
    }

//    fun getList(): RouteHandler.() -> String {
//        return {
//            val greetings = setOf<GreetingDTO>(
//                GreetingDTO("Mozart"),
//                GreetingDTO("Beethoven"),
//                GreetingDTO("Haydn")
//            )
//
//            type(contentType = "application/json")
//            JsonTransformer.render(greetings)
//        }
//    }

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