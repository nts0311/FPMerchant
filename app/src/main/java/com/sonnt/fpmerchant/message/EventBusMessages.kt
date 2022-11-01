package com.sonnt.fpmerchant.message

class EventBusMessages {
}

class LoggedInEvent(val isSessionExpired: Boolean = false)
class SessionExpiredEvent
class WSConnectedEvent