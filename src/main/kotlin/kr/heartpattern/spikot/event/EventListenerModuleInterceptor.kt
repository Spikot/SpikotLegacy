package kr.heartpattern.spikot.event

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.module.IModuleInterceptor
import kr.heartpattern.spikot.module.ModuleHandler
import kr.heartpattern.spikot.module.ModuleInterceptor
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

/**
 * Register event handler in module
 */
@ModuleInterceptor
private object EventListenerModuleInterceptor : IModuleInterceptor {
    override fun onEnable(handler: ModuleHandler) {
        Bukkit.getPluginManager().registerEvents(handler.module!!, handler.owner)
    }

    override fun onDisable(handler: ModuleHandler) {
        HandlerList.unregisterAll(handler.module!!)
    }

    override fun onError(handler: ModuleHandler, state: IModule.State, throwable: Throwable) {
        HandlerList.unregisterAll(handler.module!!)
    }
}