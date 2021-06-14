package ru.androidheroes.starthubapp.network

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*
import ru.androidheroes.starthubapp.ui.feed.EventDetails
import ru.androidheroes.starthubapp.ui.feed.Person
import ru.androidheroes.starthubapp.ui.feed.Project

interface   MoscowHackApi {

    @GET("starthub/events")
    fun getEvents(
        @Header("Authorization") token: String
    ): Observable<Response<List<EventDetails>>>

    @GET("starthub/recommended/person")
    fun getRecomendedPerson(
        @Header("Authorization") token: String
    ): Observable<Response<List<Person>>>

    @GET("starthub/projects")
    fun getProjects(
        @Header("Authorization") token: String
    ): Observable<Response<List<Project>>>
}
