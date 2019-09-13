package com.bma.gon.cadavreexquis.login

interface LoginView {

    //------------------ Cleaner
    fun cleanEverything()

    //------------------ Message
    fun showFindedUserMessage()

    fun showUnfindedUserMessage()

    fun showErrorEmail()

    fun showErrorMdp()

    fun showErrorMdpSignin()

    fun errorOnValidateMdp()

    //------------------ Texte bouton
    fun connexionTextOnLogButton()

    fun inscriptionTextOnLogButton()

    fun validateMdpTextOnLogButton()

    //------------------ Etat bouton
    fun ableButtonLogin()

    fun disableButtonLogin()

    //------------------ Action sur bouton
    fun resultButtonClicked(message:String)

    //------------------ Launch Activity
    fun launchActivity()

}