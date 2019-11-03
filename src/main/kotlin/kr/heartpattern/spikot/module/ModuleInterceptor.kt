package kr.heartpattern.spikot.module

/**
 * Module interceptor which intercept each step of module lifecycle.
 */
interface IModuleInterceptor {
    /**
     * Intercept module create
     * @param handler Target module handler
     */
    fun onCreate(handler: ModuleHandler) {}

    /**
     * Intercept module load
     * @param handler Target module handler
     */
    fun onLoad(handler: ModuleHandler) {}

    /**
     * Intercept module enable
     * @param handler Target module handler
     */
    fun onEnable(handler: ModuleHandler) {}

    /**
     * Intercept module disable
     * @param handler Target module handler
     */
    fun onDisable(handler: ModuleHandler) {}

    /**
     * Intercept module error
     * @param handler Target module handler
     * @param state State of module
     * @param throwable Thrown error
     */
    fun onError(handler: ModuleHandler, state: IModule.State, throwable: Throwable) {}
}