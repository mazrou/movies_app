package com.mazrou.movieApp.util



class Constants {

    companion object {


        const val NETWORK_TIMEOUT = 50000L
        const val CACHE_TIMEOUT = 2000L

        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"

        const val UNKNOWN_ERROR = "UNKNOWN ERROR "
        const val INVALID_PAGE = "Invalid page."
        const val INVALID_STATE_EVENT = "Invalid state event"
        const val NETWORK_ERROR = "Check your connection to the internet"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"




        fun isNetworkError(msg: String): Boolean {
            when {
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
                else -> return false
            }
        }

        fun isPaginationDone(errorResponse: String?): Boolean {
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return errorResponse?.contains(INVALID_PAGE) ?: false
        }
    }
}