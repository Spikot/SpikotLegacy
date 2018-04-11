package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.KommandFramework.KommandException

class PermissionException: KommandException()
class OnlySubCommandAllowedException: KommandException()
class ArgumentCountIncorrectException: KommandException()
class WrongArgumentException: KommandException()