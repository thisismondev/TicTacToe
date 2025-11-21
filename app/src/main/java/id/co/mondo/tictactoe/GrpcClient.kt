package id.co.mondo.tictactoe

import android.util.Log
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tictactoe.TicTacToeGrpc
import tictactoe.Tictactoe.CreateRoomRequest
import tictactoe.Tictactoe.CreateRoomResponse

class GrpcClient {

    private val channel = OkHttpChannelBuilder
        .forAddress("10.5.50.104", 50051)
//        .forAddress("10.0.2.2", 50051)
        .usePlaintext()
        .build()

    private val stub = TicTacToeGrpc.newBlockingStub(channel)

    suspend fun createRoom(player_name: String): Result<CreateRoomResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = CreateRoomRequest.newBuilder()
                    .setPlayerName(player_name)
                    .build()
                val response = stub.createRoom(request)
                Log.d("GrpcClient", "createRoom: $response")
                Log.d("GrpcClient", "createRoom: $player_name")
                Result.success(response)
            } catch (e: io.grpc.StatusRuntimeException) {
                val message = when (e.status.code) {
                    io.grpc.Status.Code.UNAVAILABLE ->
                        "Server tidak bisa dihubungi (UNAVAILABLE)"

                    io.grpc.Status.Code.DEADLINE_EXCEEDED ->
                        "Timeout: Server terlalu lambat merespon"

                    else ->
                        "gRPC error: ${e.status.code} - ${e.message}"
                }
                Log.d("GrpcClient", "createRoom: $message")
                Result.failure(Exception(message))
            } catch (e: Exception) {
                Result.failure(Exception("Error tidak diketahui: ${e.message}"))
            }
        }

}