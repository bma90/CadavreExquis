package com.bma.gon.cadavreexquis.fragments

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.regex.Pattern

class ProfilInteractor(val interactor: ProfilInteractor.OnProfilAction) {


    val db = FirebaseFirestore.getInstance()

    val PSEUDO_PATTERN: Pattern = Pattern.compile("^" +
            "(?=.*[a-zA-Z])" +      //any letter
            //"(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$")


    interface OnProfilAction {

        fun actionCheckedUser(result:Boolean)

        fun actionShowPseudo(pseudo: String)

        fun actioShowNoPseudoError()

        fun actionDisableButton()

        fun actionExplainToUserPseudoFormat()

        fun actionNotifyUserPseudoCreated()

        fun actionNotifyUserErrorPseudoCreation()

    }

    fun checkIfUserHasPseudo(id:String){
        val docRef = db.collection("players").document(id)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        document.data?.get("pseudo")?.let {
                            interactor.actionShowPseudo(it.toString())
                        } ?: run {
                            interactor.actioShowNoPseudoError()
                        }

                    } else {
                        Log.d("loginteractor", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("loginteractor", "get failed with ", exception)
                }
    }

    fun checkIfPseudoExist(pseudo:String){
        if (!PSEUDO_PATTERN.matcher(pseudo.trim()).matches()){
            interactor.actionDisableButton()
            interactor.actionExplainToUserPseudoFormat()
        }
        else{
            val docRef = db.collection("players").whereEqualTo("pseudo", pseudo)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            interactor.actionCheckedUser(document?.isEmpty)
                        } else {
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("loginteractor", "get failed with ", exception)
                    }
        }
    }

    fun saveUserPseudoInDatabase(pseudo:String, id:String){
        val data = HashMap<String, Any>()
        data["pseudo"] = pseudo

        db.collection("players").document(id).set(data, SetOptions.merge())
                .addOnSuccessListener {
                    interactor.actionNotifyUserPseudoCreated()
                    checkIfUserHasPseudo(id)
                }
                .addOnFailureListener {  }
    }

}