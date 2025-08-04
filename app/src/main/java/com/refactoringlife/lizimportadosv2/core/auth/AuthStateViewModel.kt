package com.refactoringlife.lizimportadosv2.core.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStateViewModel : ViewModel() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()
    
    init {
        checkCurrentUser()
        
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _userEmail.value = user?.email
        }
    }
    
    private fun checkCurrentUser() {
        val user = auth.currentUser
        _userEmail.value = user?.email
    }
    
    fun getCurrentUserEmail(): String? {
        return _userEmail.value
    }
} 