package app.endpoints

import app.PopMailerServiceGrpc
import jakarta.inject.Singleton

@Singleton
class GrpcPopMailerEndpoint: PopMailerServiceGrpc.PopMailerServiceImplBase() // TODO
