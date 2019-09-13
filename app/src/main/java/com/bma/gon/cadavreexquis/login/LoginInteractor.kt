package com.bma.gon.cadavreexquis.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class LoginInteractor {

    //------------------ Interface classe
    interface OnLoginActions {

        // ------- For the view to call presenter which call Interactor after
        fun actionCheckLogin(login:String, mdp: String)

        fun actionSubscribeLoginClicked_SignIn(login:String, mdp:String)

        fun actionSubscribeLoginClicked_SignUp(login:String, mdp:String)

        fun actionSubscribeLoginClicked_SignUp_Validate(login:String, mdpToConfirm:String, mdp:String)

        fun actionCheckLoginMdp(login:String, mdp:String)

        // ------- For the intercator to call presenter which call the view after
        fun setErrorLogin()

        fun setErrorMdp()

        fun setErrorMdpSignin()

        fun changeButtonLoginText(boolean: Boolean)

        fun changeStatutButon(bool:Boolean)

        fun askToSendMessage(message:String)

        fun askToValidateMdp()

        fun responseOfTheValidateMdp(validate:Boolean)

        fun askTolaunchActivity()

        fun askToCleanEverything()
        
    }

    //------------------ Variable
    val db = FirebaseFirestore.getInstance()

    val mAuth:FirebaseAuth = FirebaseAuth.getInstance()

    val PASSWORD_PATTERN: Pattern = Pattern.compile("^" +
            "(?=.*[a-zA-Z])" +      //any letter
            //"(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$")

    //------------------ Fonctions de traitements des intéractions

    /**
     * Fonction qui va vérifier que si le login existe dans la database
     *
     * @param pseudo: correspond à l'id user à vérifier
     */
    fun checkDatabase(listenerHer:OnLoginActions, login:String){
        val docRef = db.collection("players").whereEqualTo("email", login)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        listenerHer.changeButtonLoginText(document?.isEmpty)
                    } else {
                        Log.d("loginteractor", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("loginteractor", "get failed with ", exception)
                }
    }

    /**
     * Fonction qui va traité de l'activation du bouton ou non
     *
     * @param login: Login de type String
     * @param mdp: mdp de type String
     * @param listernerHer: Listener de type interface OnLoginAction (utilisé par le Presenter)
     */
    fun checkLogMdp(login:String, mdp:String, listernerHer:OnLoginActions){
        if (login.isNotEmpty() && mdp.isNotEmpty())
            listernerHer.changeStatutButon(true)
        else
            listernerHer.changeStatutButon(false)
    }


    /**
     * Fonction qui traite l'action lorsque l'utilisateur a cliqué sur le bouton Inscription/Connexion
     * elle vérifie que tout les champs respectent les critères
     *
     * @param value: login
     * @param mdp: mot de passe
     * @param listenerHer: interface pour utiliser le loginPresenter
     */
    fun valideFormatInfoUser(value: String, mdp:String, listenerHer:OnLoginActions){

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            listenerHer.setErrorLogin()
            return
        }

        if(!PASSWORD_PATTERN.matcher(mdp).matches()){
            listenerHer.setErrorMdp()
            return
        }

        listenerHer.askToValidateMdp()

    }

    /**
     * Fonction qui traite l'action lorsque l'utilisateur a cliqué sur le bouton Inscription/Connexion
     * elle vérifie que tout les champs respectent les critères
     *
     * @param value: login
     */
    fun inscriptionUser(login: String, mdpToConfirm:String, mdp:String, listenerHer:OnLoginActions){

        if (mdpToConfirm.equals(mdp)){
            launchInscription(login, mdp, listenerHer)
        }
        else{
            listenerHer.responseOfTheValidateMdp(false)
        }

    }

    fun connexionUser(login:String, mdp:String, listenerHer: OnLoginActions){
        mAuth.signInWithEmailAndPassword(login, mdp)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listenerHer?.askTolaunchActivity()
                    } else {
                        listenerHer?.setErrorMdpSignin()
                    }
                }
    }

    //------------------ Fonctions private

    private fun launchInscription(login: String, mdp:String, listenerHer:OnLoginActions){
        mAuth.createUserWithEmailAndPassword(login, mdp)
                .addOnCompleteListener {
                    if (it.isSuccessful()){
                        listenerHer.askToSendMessage("Inscription réussie")
                        saveUserInDatabase(login)
                        listenerHer?.askTolaunchActivity()
                    }else{
                        listenerHer.askToSendMessage("Un problème est survenu durant l'inscription")
                        listenerHer.askToCleanEverything()
                    }
                }
    }

    private fun saveUserInDatabase(login:String){
        val data = HashMap<String, Any>()
        data["email"] = login

        db.collection("players").document(""+mAuth.currentUser?.uid).set(data)
    }

}