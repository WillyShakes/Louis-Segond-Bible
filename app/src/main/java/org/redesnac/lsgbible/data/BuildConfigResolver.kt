package org.redesnac.lsgbible.data


import org.redesnac.lsgbible.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigResolver @Inject constructor() {
    val isDebug: Boolean
        get() = BuildConfig.DEBUG
}