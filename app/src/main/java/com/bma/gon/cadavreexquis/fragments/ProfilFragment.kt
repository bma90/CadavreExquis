package com.bma.gon.cadavreexquis.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bma.gon.cadavreexquis.R
import com.bma.gon.cadavreexquis.animation.AnimationsUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profil.*


class ProfilFragment : Fragment(), ProfilInteractor.OnProfilAction {

    val profilInteractor:ProfilInteractor = ProfilInteractor(this)
    lateinit var animation: AnimationsUtils
    lateinit var idUser:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        animation = AnimationsUtils(this.requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilInteractor.checkIfUserHasPseudo(idUser)


        pseudo_btn.setOnClickListener(onClickButtonListener)

        pseudo_profil.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                    profilInteractor.checkIfPseudoExist(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    // ------------------ Gesture Function
    private val onClickButtonListener = View.OnClickListener {
        profilInteractor.saveUserPseudoInDatabase(pseudo_profil.text.toString().trim(), idUser)
    }

    // ------------------ Override Function
    /**
     * Notifie l'utilisateur que le pseudo a pas été ajouté
     */
    override fun actionNotifyUserErrorPseudoCreation() {
        animation.showNewText("Alalala, j'ai pas pu créer ton pseudo, dsl ! ☹️", comWU_profil)
    }

    /**
     * Notifie l'utilisateur que le pseudo a bien étét ajouté en bdd
     */
    override fun actionNotifyUserPseudoCreated() {
        animation.showNewText("C'est bon t'as un pseudo maintenant, vas t'amuser ! \uD83D\uDE09", comWU_profil)
    }

    override fun actionExplainToUserPseudoFormat() {
        animation.showNewText("Donc je t'explique, je veux pas moins de 4 caractères et pas d'espaces \uD83D\uDE36", comWU_profil)
    }

    /**
     * Bouton à désactiver
     */
    override fun actionDisableButton() {
        pseudo_btn.isEnabled = false
    }

    /**
     * Bouton activer
     */
    private fun actionAbleButton(){
        pseudo_btn.isEnabled = true
    }

    /**
     * Fonction qui communique avant validation à l'utilisateur si le pseudo choisi est déjà utiliser ou non
     *
     * @param result: boolean, true(libre), false(pas utilisé)
     */
    override fun actionCheckedUser(result: Boolean) {
        if (result){
            animation.showNewText("Ce pseudo est libre, fonnnnnce !!! \uD83D\uDE03", comWU_profil)
            actionAbleButton()
        }
        else{
            animation.showNewText("Creuses un peu plus, psk ce pseudo est déjà utilisé ! \uD83D\uDE15", comWU_profil)
            actionDisableButton()
        }
    }

    /**
     * Fonction qui affiche le pseudo et cache le choix du pseudo
     *
     * @param pseudo: Le pseudo de l'user
     */
    override fun actionShowPseudo(pseudo: String) {
        pseudo_title.text = pseudo
        pseudo_title.visibility = View.VISIBLE
        form_pseudo.visibility = View.GONE
    }

    /**
     * Fonction qui affiche le form du pseudo
     */
    override fun actioShowNoPseudoError() {
        animation.showNewText("Hey ouai poto t'as cru t'allais jouer sans pseudo ?! \uD83E\uDD23", comWU_profil)
        pseudo_title.visibility = View.GONE
        form_pseudo.visibility = View.VISIBLE
    }

}
