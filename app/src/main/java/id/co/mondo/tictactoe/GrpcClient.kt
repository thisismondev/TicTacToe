package id.co.mondo.tictactoe

import android.util.Log
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tictactoe.TicTacToeGrpcKt
import tictactoe.Tictactoe.CreateRoomRequest
import tictactoe.Tictactoe.CreateRoomResponse
import tictactoe.Tictactoe.JoinRoomRequest
import tictactoe.Tictactoe.JoinRoomResponse
import tictactoe.Tictactoe.MakeMoveRequest
import tictactoe.Tictactoe.MakeMoveResponse
import tictactoe.Tictactoe.RoomUpdate
import tictactoe.Tictactoe.StartGameRequest
import tictactoe.Tictactoe.StartGameResponse
import tictactoe.Tictactoe.SubscribeRoomRequest

class GrpcClient {

    private val channel = OkHttpChannelBuilder
        .forAddress("34.46.75.229", 50051)
//        .forAddress("10.0.2.2", 50051)
        .usePlaintext()
        .build()

    private val stub = TicTacToeGrpcKt.TicTacToeCoroutineStub(channel)

    suspend fun createRoom(playerName: String): Result<CreateRoomResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = CreateRoomRequest.newBuilder()
                    .setPlayerName(playerName)
                    .build()
                val response = stub.createRoom(request)
                Log.d("GrpcClient", "createRoom: $playerName")
                Log.d("GrpcClient", "createRoom: $response")
                Result.success(response)
            } catch (e: Exception) {
                Log.e("GrpcClient", "createRoom error: ${e.message}")
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

    suspend fun joinRoom(playerName: String, roomId: String): Result<JoinRoomResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = JoinRoomRequest.newBuilder()
                    .setPlayerName(playerName)
                    .setRoomId(roomId)
                    .build()
                val response = stub.joinRoom(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

    suspend fun startGame(roomId: String): Result<StartGameResponse> =
        withContext(Dispatchers.IO){
            try{
                val request = StartGameRequest.newBuilder()
                    .setRoomId(roomId)
                    .build()
                val response = stub.startGame(request)
                Result.success(response)
            }
            catch (e: Exception){
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

    suspend fun makeMove(
        roomId: String,
        playerName: String,
        row: Int,
        col: Int
    ): Result<MakeMoveResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = MakeMoveRequest.newBuilder()
                    .setRoomId(roomId)
                    .setPlayerName(playerName)
                    .setRow(row)
                    .setCol(col)
                    .build()
                val response = stub.makeMove(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

    suspend fun subscribeRoom(roomId: String): Result<Flow<RoomUpdate>> =
        withContext(Dispatchers.IO) {
            try {
                val request = SubscribeRoomRequest.newBuilder()
                    .setRoomId(roomId)
                    .build()
                val response = stub.subscribeRoom(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

}