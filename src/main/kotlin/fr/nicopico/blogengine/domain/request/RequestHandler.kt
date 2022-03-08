package fr.nicopico.blogengine.domain.request

interface RequestHandler<in Request, out Response> {
    fun handle(request: Request): Response
}

interface CommandHandler<in Request> : RequestHandler<Request, Unit>
