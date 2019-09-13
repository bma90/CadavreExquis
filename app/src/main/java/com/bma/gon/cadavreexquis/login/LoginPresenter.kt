package com.bma.gon.cadavreexquis.login

class LoginPresenter(val loginView:LoginView?, val loginInteractor:LoginInteractor): LoginInteractor.OnLoginActions {

    //------------------ Override

    /**
     * Fonction qui appelle l'interactor pour vérifier que le login existe dans la BDD et qui active ou désactive le bouton
     *
     * @param login: login de l'utilisateur
     */
    override fun actionCheckLogin(login: String, mdp:String) {
        if(login.isNotEmpty()) {
            loginInteractor.checkDatabase(this, login)
        }
        actionCheckLoginMdp(login, mdp)
    }

    /**
     * Fonction qui appelle l'intercator pour verifier que le couple login/mdp est bon, pour la connexion
     *
     * @param login: Login de type String
     * @param mdp: Mdp de type String
     */
    override fun actionSubscribeLoginClicked_SignIn(login: String, mdp: String) {
        loginInteractor.connexionUser(login, mdp, this)
    }

    /**
     * Fonction qui appelle l'intercator pour verifier que les login et mdp ont été renseigné (sert pour l'activation du bouton de connexion
     *
     * @param login: Login de type String
     * @param mdp: Mdp de type String
     */
    override fun actionCheckLoginMdp(login: String, mdp: String) {
        loginInteractor.checkLogMdp(login, mdp, this)
    }

    /**
     * Fonction qui demande à la vue d'agir après que le bouton de la page login soit clické
     * Appelle le login intercator pour la vérification des infos
     *
     * @param login: login rempli par l'utilisateur
     * @param mdp: mdp rempli par l'utilisateur
     */
    override fun actionSubscribeLoginClicked_SignUp(login:String, mdp:String) {
        loginInteractor.valideFormatInfoUser(login, mdp, this)
    }

    /**
     * Fonction qui demande à la vue d'agir après que le bouton de la page login soit clické
     * Appelle le login intercator pour vérifier que le mot de passe est identique
     *
     * @param login: login rempli par l'utilisateur
     * @param mdpToConfirm: mdp rempli par l'utilisateur à valider
     * @param mdp: mdp de validation
     */
    override fun actionSubscribeLoginClicked_SignUp_Validate(login: String, mdpToConfirm: String, mdp: String) {
        loginInteractor.inscriptionUser(login, mdpToConfirm, mdp, this)
    }

    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------- Called by Interactor -------------------------------------------
    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    override fun askToCleanEverything() {
        loginView?.cleanEverything()
    }

    override fun setErrorMdpSignin() {
        loginView?.showErrorMdpSignin()
    }

    override fun askTolaunchActivity() {
        loginView?.launchActivity()
    }

    override fun responseOfTheValidateMdp(validate: Boolean) {
        if (!validate){
            loginView?.errorOnValidateMdp()
        }
    }

    /**
     * Fonction qui demande à la vue de modifier le statut du bouton entre Activé et désactivé
     *
     * @param bool: Boolean si true --> on active le bouton sinon l'inverse
     */
    override fun changeStatutButon(bool: Boolean) {
        if (bool)
            loginView?.ableButtonLogin()
        else
            loginView?.disableButtonLogin()
    }

    /**
     * Fonction qui demande à la vue de changer l'état du bouton entre connection et inscription, change aussi le texte de comm'
     *
     * @param boolean: boolean, true:inscription & false:connection
     */
    override fun changeButtonLoginText(boolean: Boolean) {
        if (boolean){
            loginView?.inscriptionTextOnLogButton()
            loginView?.showUnfindedUserMessage()
        }
        else {
            loginView?.showFindedUserMessage()
            loginView?.connexionTextOnLogButton()
        }
    }

    /**
     * Foncion qui demande à la vue d'afficher une erreur à l'utilisateur concernant son Email
     */
    override fun setErrorLogin() {
        loginView?.showErrorEmail()
    }

    /**
     * Foncion qui demande à la vue d'afficher une erreur à l'utilisateur concernant son Mdp
     */
    override fun setErrorMdp() {
        loginView?.showErrorMdp()
    }

    /**
     * Fonction qui demande à la vue d'afficher le résultat du l'inscription
     */
    override fun askToSendMessage(message: String) {
        loginView?.resultButtonClicked(message)
    }

    /**
     * Fonction qui va changer le texte du bouton
     */
    override fun askToValidateMdp() {
        loginView?.validateMdpTextOnLogButton()
    }

}