package io.github.ReadyMadeProgrammer.Spikot.command

import org.bukkit.permissions.Permissible
import org.bukkit.permissions.Permission

fun requirePermission(permission: Permission): Verifier<Permissible> = { p: Permissible -> p.hasPermission(permission) }

fun requirePermission(permission: String): Verifier<Permissible> = requirePermission(Permission(permission))