package fr.nicopico.blogengine.domain.request

import org.springframework.beans.factory.BeanFactory
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class Dispatcher(
    private val beanFactory: BeanFactory
) {
    fun <Request, Response> dispatch(
        handler: KClass<out RequestHandler<Request, Response>>,
        request: Request,
    ): Response = handler.get().handle(request)

    private fun <T : Any> KClass<T>.get() = beanFactory.getBean(this.java)
}
