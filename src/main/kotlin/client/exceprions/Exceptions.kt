package client.exceprions

class InvalidRequestData : RuntimeException("Invalid request data")
class UserNotFound: NullPointerException("User not found")
class ChatNotFound: NullPointerException("Chat not found")
class MessageNotFound: NullPointerException("Message not found")
class AccessDenied: RuntimeException("Access denied")
class UnregisteredAccount: NullPointerException("ID is not registered")