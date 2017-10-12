/*
 * Copyright (c) 2016, 2017, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.truffle.llvm.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.llvm.runtime.debug.LLVMSourceType;
import com.oracle.truffle.llvm.runtime.types.Type;

@ValueType
public final class LLVMTruffleObject {
    private final TruffleObject object;
    private final long offset;
    private final Type type;
    private final LLVMSourceType baseType;

    private static LLVMSourceType overrideBaseType(LLVMTruffleObject obj, Type newType) {
        if (obj.getOffset() == 0) {
            if (newType.getSourceType() != null) {
                return newType.getSourceType();
            } else {
                return obj.getBaseType();
            }
        } else {
            return obj.getBaseType();
        }
    }

    public LLVMTruffleObject(LLVMTruffleObject orig, Type type) {
        this(orig.getObject(), orig.getOffset(), type, overrideBaseType(orig, type));
    }

    public LLVMTruffleObject(TruffleObject object, Type type) {
        this(object, 0, type, type.getSourceType());
    }

    public LLVMTruffleObject(TruffleObject object, long offset, Type type, LLVMSourceType baseType) {
        this.object = object;
        this.offset = offset;
        this.type = type;
        this.baseType = baseType;
    }

    public long getOffset() {
        return offset;
    }

    public TruffleObject getObject() {
        return object;
    }

    public Type getType() {
        return type;
    }

    public LLVMTruffleObject increment(long incr, Type newType) {
        return new LLVMTruffleObject(object, offset + incr, newType, baseType);
    }

    public LLVMSourceType getBaseType() {
        return baseType;
    }

    @TruffleBoundary
    @Override
    public String toString() {
        return super.toString() + "(offset=" + getOffset() + ")";
    }

}
