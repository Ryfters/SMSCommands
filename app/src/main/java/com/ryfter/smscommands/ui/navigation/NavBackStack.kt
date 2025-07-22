package com.ryfter.smscommands.ui.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList

typealias MyNavBackStack = SnapshotStateList<Route>

fun MyNavBackStack.navigate(route: Route) = add(route)

fun MyNavBackStack.pop() = removeAt(this.lastIndex)

fun MyNavBackStack.replace(route: Route) {
    this[this.lastIndex] = route
}

fun MyNavBackStack.set(route: Route) {
    clear()
    add(0, route)
}

fun MyNavBackStack.set(routes: List<Route>) {
    clear()
    addAll(0, routes)
}
