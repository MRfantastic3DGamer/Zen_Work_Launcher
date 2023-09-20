package com.zenworklauncher.widgets.quick_action.data.local

sealed class QuickActionsType{
    sealed class Navigation : QuickActionsType(){
        object Home : Navigation(){
            val AllApps : Navigation = Home
            val Settings  : Navigation = Home
        }
        object Schedule : Navigation(){

        }
    }

    sealed class Actions : QuickActionsType(){
        object Home : Actions(){

        }
        object Schedule : Actions(){
            val AddNew : Actions = Schedule
            val Delete : Actions = Schedule
            val Edit   : Actions = Schedule
        }
    }
}