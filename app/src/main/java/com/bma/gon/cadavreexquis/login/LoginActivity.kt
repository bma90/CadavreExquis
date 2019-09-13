package com.bma.gon.cadavreexquis.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bma.gon.cadavreexquis.activities.MainActivity
import com.bma.gon.cadavreexquis.R
import com.bma.gon.cadavreexquis.animation.AnimationsUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    //------------------ Variables
    lateinit var loginPresenter: LoginPresenter

    lateinit var animation:AnimationsUtils

    var mdpToValidate:String = ""

    // ------------------ Override AppCompat classe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        animation = AnimationsUtils(this)

        loginPresenter = LoginPresenter(this, LoginInteractor())

        /**
         * Listener sur l'EditText du login
         */
        pseudo_profil.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(!login_btn.text.toString().equals("Valider"))
                    loginPresenter.actionCheckLogin(s.toString().trim(), mdp_accueil.text.toString().trim())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                login_accueil_til.error = null
            }

        })

        /**
         * Listener sur l'EditText du mdp
         */
        mdp_accueil.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                loginPresenter.actionCheckLoginMdp(pseudo_profil.text.toString().trim(), s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mdp_accueil_til.error = null
            }

        })

        login_btn.setOnClickListener(clickOnLoginButon)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mdpToValid")){
                mdpToValidate = savedInstanceState.getString("mdpToValid")
                login_btn.text = "Valider"
                comWU_login.text = "Valide d'abord ton mot de passe et après je te laisse, promis ! \uD83D\uDE09"
                pseudo_profil.isEnabled = false
            }
        }

    }

    override fun onStart() {
        super.onStart()
        //TODO à utiliser pour la redirection si connecté
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            launchActivity()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (mdpToValidate.isNotEmpty()&& login_btn.text.toString().equals("Valider"))
            outState?.putString("mdpToValid", mdpToValidate)
    }

    //------------------ Gesture function
    private val clickOnLoginButon = View.OnClickListener {
        val pseudo:String = pseudo_profil.text.toString().trim()
        val mdp:String = mdp_accueil.text.toString().trim()
        when(login_btn.text.toString()){
            "Je m'inscris !" -> loginPresenter.actionSubscribeLoginClicked_SignUp(pseudo, mdp)
            "Valider" -> loginPresenter.actionSubscribeLoginClicked_SignUp_Validate(pseudo, mdpToValidate, mdp)
            "Je me connecte !" -> loginPresenter.actionSubscribeLoginClicked_SignIn(pseudo, mdp)
        }
    }

    // ------------------ Function
    private fun showErrorValidateMdp(){
        animation.showNewText("Waaaaa le gars il sait même pas valider un mot de passe, faut recommencer maintenant \uD83D\uDE29", comWU_login)
    }

    private fun showErrorEverythingOk(){
        animation.showNewText("Tout est ok, mais j'ai pas pu t'enregistrer, contact moi pour qu'on résoud le problème \uD83E\uDD13", comWU_login)
    }

    // ------------------ Override function
        //------------------ Cleaner
    override fun cleanEverything() {
        inscriptionTextOnLogButton()
        showErrorEverythingOk()
        pseudo_profil.isEnabled = true
    }

        //------------------ Message
    override fun showFindedUserMessage() {
        animation.showNewText("Ah tu me dis quelque chose ! Connecte toi ! \uD83D\uDE01", comWU_login)
    }

    override fun showUnfindedUserMessage() {
        animation.showNewText("Inscris toi ou connecte toi si t'es du club \uD83D\uDE09", comWU_login)
    }

    override fun showErrorEmail() {
        animation.showNewText("Genre ça c'est une adresse mail ? \uD83E\uDD14 ", comWU_login)
        login_accueil_til.setError("Mauvais format d'adresse mail")
    }

    override fun showErrorMdp() {
        animation.showNewText("Ah ! Ton mot de passe doit contenir au moins 6 caractères sans espaces. DSL \uD83D\uDE2C", comWU_login)
        mdp_accueil_til.setError("Mauvais format de mot de passe")
    }

    override fun showErrorMdpSignin() {
        animation.showNewText("Waaaaa, tu connais pas ton mot de passe ? \uD83D\uDE12", comWU_login)
    }

    override fun errorOnValidateMdp() {
        inscriptionTextOnLogButton()
        showErrorValidateMdp()
        pseudo_profil.isEnabled = true
    }

        //------------------ Texte bouton
    override fun connexionTextOnLogButton() {
        login_btn.text = "Je me connecte !"
    }

    override fun inscriptionTextOnLogButton() {
        login_btn.text = "Je m'inscris !"
    }

    override fun validateMdpTextOnLogButton() {
        login_btn.text = "Valider"
        animation.showNewText("Valide d'abord ton mot de passe et après on commence, promis ! \uD83D\uDE09", comWU_login)
        mdpToValidate = mdp_accueil.text.toString()
        mdp_accueil.setText("")
        pseudo_profil.isEnabled = false
    }

        //------------------ Etat bouton
    override fun ableButtonLogin() {
        login_btn.isEnabled = true
    }

    override fun disableButtonLogin() {
        login_btn.isEnabled = false
    }

        //------------------ Action sur bouton
    override fun resultButtonClicked(message: String) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

        //------------------ Launch Activity
    override fun launchActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}
