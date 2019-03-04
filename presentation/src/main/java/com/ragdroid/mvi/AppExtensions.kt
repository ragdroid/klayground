package com.ragdroid.mvi

infix fun <T>Boolean.then(action : () -> T): T? {
    return if (this)
        action.invoke()
    else null
}

infix fun <T>T?.elze(action: () -> T): T {
    return if (this == null)
        action.invoke()
    else this
}
