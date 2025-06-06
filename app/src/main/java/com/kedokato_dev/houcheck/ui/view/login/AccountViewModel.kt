package com.kedokato_dev.houcheck.ui.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kedokato_dev.houcheck.repository.AccountRepository
import com.kedokato_dev.houcheck.local.entity.AccountEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository
): ViewModel() {


    fun insertAccount(account: AccountEntity) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    suspend fun getAllAccounts(): Result<AccountEntity> {
        return try {
            val result: Result<AccountEntity> = repository.getAccount()
            result // Trả về kết quả
        } catch (error: Exception) {
            Result.failure(error) // Trả về lỗi
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            repository.deleteAllAccounts()
        }
    }

    suspend fun checkAccountExist(userName: String): Boolean {
        return try {
            repository.checkAccountExist(userName)
        } catch (error: Exception) {
            false
        }
    }
}

class AccountViewModelFactory(
    private val repository: AccountRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}