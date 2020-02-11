/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.utils

@Suppress("SpellCheckingInspection")
private const val allowedTag = "abcdefghijlmnopqstuvwxyz0123456789"
private const val allowedLength = allowedTag.length
private const val special = '§'
private const val bounder = "§r"

/**
 * Attach invisible number after string
 * @receiver String to attach invisible number
 * @param i Number to be attached
 * @return String with invisible [i] attached
 */
fun String.attachInvisible(i: Int): String {
    return this + i.encryptInvisible()
}

/**
 * Encrypt number to invisible number
 * @receiver Number to encrypt
 * @return Encrypted number
 */
fun Int.encryptInvisible(): String {
    var i = this
    val builder = StringBuilder()
    builder.append(bounder)
    while (i != 0) {
        builder.append(special + allowedTag[i % allowedLength].toString())
        i /= allowedLength
    }
    builder.append(bounder)
    return builder.toString()
}

/**
 * Decrypt invisible number to number
 * @receiver String to decrypt
 * @return Decrypted number
 */
fun String.decryptInvisible(): Int {
    if (this.length < 6 || !this.startsWith(bounder) && !this.endsWith(bounder)) {
        throw IllegalArgumentException("Illegal Format")
    }
    val code = this.substring(2, this.length - 2).replace(special.toString(), "").reversed()
    var i = 0
    code.forEach {
        i *= allowedLength
        i += allowedTag.indexOf(it)
    }
    return i
}

/**
 * Find invisible number and decrypt it
 * @receiver String to find invisible number
 * @return Founded invisible number
 */
fun String.findInvisible(): Int {
    var i = this.length - 1
    if (this[i] != 'r' || this[i - 1] != '§') throw IllegalArgumentException("Cannot found invisible code")
    i -= 2
    while (!(this[i] == 'r' && this[i - 1] == '§') && i >= 1) {
        if (!allowedTag.contains(this[i]) || this[i - 1] != '§') {
            throw IllegalArgumentException("Cannot found invisible code")
        }
        i -= 2
    }
    i--
    return this.substring(i).decryptInvisible()
}

/**
 * Check string contains invisible number
 * @receiver String to check
 * @return Whether string contains invisible number
 */
fun String.hasInvisible(): Boolean {
    try {
        this.findInvisible()
    } catch (e: Exception) {
        return false
    }
    return true
}