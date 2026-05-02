package com.example.gullycricket.core.network

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

@Serializable
data class LiveScoreDto(
    val team1Name: String,
    val team2Name: String,
    val currentScore: Int,
    val wickets: Int,
    val currentOver: Int,
    val ballsBowled: Int
)

class LocalScoringServer(private val liveScoreFlow: StateFlow<LiveScoreDto?>) {
    private var server: NettyApplicationEngine? = null

    fun startServer(port: Int = 8080) {
        if (server != null) return
        server = embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                get("/api/score") {
                    val score = liveScoreFlow.value
                    if (score != null) {
                        call.respond(score)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "No active match")
                    }
                }
                
                get("/") {
                    call.respondText(
                        contentType = ContentType.Text.Html,
                        text = """
                            <!DOCTYPE html>
                            <html lang="en">
                                <head>
                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                    <title>Gully Cricket Live</title>
                                    <style>
                                        body { font-family: 'Roboto', sans-serif; background: #121212; color: #ffffff; text-align: center; padding-top: 50px; }
                                        .card { background: #1E1E1E; padding: 20px; border-radius: 12px; display: inline-block; box-shadow: 0 4px 6px rgba(0,0,0,0.3); }
                                        h1 { margin: 0 0 10px 0; color: #BB86FC; }
                                        .score { font-size: 48px; font-weight: bold; }
                                        .overs { font-size: 24px; color: #03DAC6; }
                                    </style>
                                </head>
                                <body>
                                    <div class="card">
                                        <h1 id="matchTitle">Match Live</h1>
                                        <div class="score" id="score">0/0</div>
                                        <div class="overs" id="overs">0.0 Overs</div>
                                    </div>
                                    <script>
                                        setInterval(() => {
                                            fetch('/api/score')
                                                .then(res => {
                                                    if (!res.ok) throw new Error("No active match");
                                                    return res.json();
                                                })
                                                .then(data => {
                                                    document.getElementById('matchTitle').innerText = data.team1Name + " vs " + data.team2Name;
                                                    document.getElementById('score').innerText = data.currentScore + "/" + data.wickets;
                                                    document.getElementById('overs').innerText = data.currentOver + "." + data.ballsBowled + " Overs";
                                                })
                                                .catch(err => {
                                                    document.getElementById('matchTitle').innerText = "Waiting for match...";
                                                });
                                        }, 1000);
                                    </script>
                                </body>
                            </html>
                        """.trimIndent()
                    )
                }
            }
        }.start(wait = false)
    }

    fun stopServer() {
        server?.stop(1000, 2000)
        server = null
    }
}
