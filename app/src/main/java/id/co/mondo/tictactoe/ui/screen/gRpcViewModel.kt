package id.co.mondo.tictactoe.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mondo.tictactoe.GrpcClient
import id.co.mondo.tictactoe.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tictactoe.Tictactoe
import tictactoe.Tictactoe.CreateRoomResponse
import tictactoe.Tictactoe.RoomUpdate

class gRpcViewModel : ViewModel() {

    private val client = GrpcClient()

    private val _room = MutableStateFlow<UiState<CreateRoomResponse?>>(UiState.Empty)
    val room: StateFlow<UiState<CreateRoomResponse?>> = _room

    private val _joinRoom = MutableStateFlow<UiState<Tictactoe.JoinRoomResponse?>>(UiState.Empty)
    val masukRoom: StateFlow<UiState<Tictactoe.JoinRoomResponse?>> = _joinRoom

    private val _roomUpdate = MutableStateFlow<UiState<RoomUpdate?>>(UiState.Empty)
    val roomUpdate = _roomUpdate.asStateFlow()

    private val _roomId = MutableStateFlow("")
    val roomId = _roomId.asStateFlow()

    private val _navigateToPlay = MutableStateFlow("")
    val navigateToPlay = _navigateToPlay.asStateFlow()

    private val _roomLocked = MutableStateFlow(false)
    val roomLocked = _roomLocked.asStateFlow()

    fun lockRoom() {
        _roomLocked.value = true
    }

    fun createRoom(player_name: String) {
        viewModelScope.launch {
            _room.value = UiState.Loading
            Log.d("createViewModel", "createRoom: $player_name")
            val result = client.createRoom(player_name)
            result.onSuccess {
                _room.value = UiState.Success(it)
                val id = it.room.roomId
                Log.d("createViewModel", "createRoom: $id")
                _roomId.value = id
                subscribeRoom(id)
                lockRoom()
            }
            result.onFailure {
                _room.value = UiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun joinRoom(player_name: String, room_id: String) {
        viewModelScope.launch {
            _joinRoom.value = UiState.Loading
            val result = client.joinRoom(player_name, room_id)
            result.onSuccess {
                _joinRoom.value = UiState.Success(it)
                subscribeRoom(room_id)
                Log.d("joinViewModel", "joinRoom: $it")
                lockRoom()
            }
            result.onFailure {
                _joinRoom.value = UiState.Error(it.message ?: "Unknown error")
            }

        }
    }


    fun startGame(id: String = roomId.value) {
        if (id.isEmpty()) {
            Log.e("startViewModel", "Room ID kosong, tidak bisa Memulai permainan")
            return
        }
        Log.d("startViewModel", "startGame: $id")
        viewModelScope.launch {
            val result = client.startGame(id)
            result.onSuccess {
                subscribeRoom(id)
                Log.d("startViewModel", "startGame: $it")
            }
            result.onFailure {
                Log.e("startViewModel", "startGame error: ${it.message}")
            }
        }
    }

    fun subscribeRoom(id: String = roomId.value) {
        if (id.isEmpty()) {
            Log.e("subscribeViewModel", "Room ID kosong, tidak bisa subscribe")
            return
        }
        viewModelScope.launch {
            val result = client.subscribeRoom(id)
            result.onSuccess { flow ->
                flow.collect { update ->
                    Log.d("subscribeViewModel", "Room Update Received: $update")
                    _roomUpdate.value = UiState.Success(update)
                    _navigateToPlay.value = update.room.status
                    Log.d("subscribeViewModel", "Room Status: $navigateToPlay")
                }
            }
            result.onFailure {
                Log.e("subscribeViewModel", "SubscribeRoom error: ${it.message}")
            }
        }
    }


}