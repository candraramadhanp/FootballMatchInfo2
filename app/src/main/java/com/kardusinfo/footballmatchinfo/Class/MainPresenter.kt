package com.kardusinfo.footballmatchinfo.Class

import com.google.gson.Gson
import com.kardusinfo.footballmatchinfo.MainApi.ApiRepository
import com.kardusinfo.footballmatchinfo.MainApi.MainApi
import com.kardusinfo.footballmatchinfo.Interface.MainView
import com.kardusinfo.footballmatchinfo.Specials.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainPresenter(private val view: MainView) {
    val repository = ApiRepository()
    val gson = Gson()
    var leagues: MutableList<League> = mutableListOf()

    fun getLeagueList() {
        view.showLoading()
        doAsync {
            val data: List<League>? = gson.fromJson(repository.doRequest(MainApi.getLeagues()),
                    LeagueResponse::class.java).leagues

            for (league in data!!.iterator()) {
                if (league.type.equals("Soccer")) {
                    leagues.add(league)
                }
            }

            uiThread {
                view.hideLoading()
                data?.let { view.showLeagueList(leagues) }
            }
        }
    }

    fun getTeamList(league: String?) {
        view.showLoading()
        doAsync {
            val data: List<Team>? = gson.fromJson(repository.doRequest(MainApi.getTeams(league)),
                    TeamResonse::class.java
            ).teams

            uiThread {
                view.hideLoading()
                data?.let { view.showTeamList(data) }
            }
        }
    }

    fun getLastMatch(leagueId: String?) {
        view.showLoading()
        doAsync {
            val data: List<Event>? = gson.fromJson(repository.doRequest((MainApi.getLastMatches(leagueId))),
                    EventResponse::class.java).events

            uiThread {
                view.hideLoading()
                data?.let { view.showMatchList(data) }
            }
        }
    }

    fun getNextMatch(leagueId: String?) {
        view.showLoading()
        doAsync {
            val data: List<Event>? = gson.fromJson(repository.doRequest(MainApi.getNextMatches(leagueId)),
                    EventResponse::class.java).events

            uiThread {
                view.hideLoading()
                data?.let { it -> view.showMatchList(it) }
            }
        }
    }
}