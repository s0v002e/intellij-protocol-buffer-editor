/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devtools.intellij.protoeditor.lang.stub.type;

import com.google.devtools.intellij.protoeditor.lang.psi.PbEnumDefinition;
import com.google.devtools.intellij.protoeditor.lang.psi.impl.PbEnumDefinitionImpl;
import com.google.devtools.intellij.protoeditor.lang.stub.PbEnumDefinitionStub;
import com.google.devtools.intellij.protoeditor.lang.stub.index.QualifiedNameIndex;
import com.google.devtools.intellij.protoeditor.lang.stub.index.ShortNameIndex;
import com.intellij.lang.Language;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.QualifiedName;
import com.intellij.util.io.StringRef;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class PbEnumDefinitionType extends IStubElementType<PbEnumDefinitionStub, PbEnumDefinition> {

  PbEnumDefinitionType(String debugName, Language language) {
    super(debugName, language);
  }

  @Override
  public PbEnumDefinition createPsi(@NotNull PbEnumDefinitionStub stub) {
    return new PbEnumDefinitionImpl(stub, this);
  }

  @Override
  public PbEnumDefinitionStub createStub(
      @NotNull PbEnumDefinition psi, @SuppressWarnings("rawtypes") StubElement parentStub) {
    return new PbEnumDefinitionStub(parentStub, this, psi.getName());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "protobuf.enum";
  }

  @Override
  public void serialize(@NotNull PbEnumDefinitionStub stub, @NotNull StubOutputStream dataStream)
      throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public PbEnumDefinitionStub deserialize(
      @NotNull StubInputStream dataStream, @SuppressWarnings("rawtypes") StubElement parentStub)
      throws IOException {
    String name = null;
    StringRef nameRef = dataStream.readName();
    if (nameRef != null) {
      name = nameRef.getString();
    }
    return new PbEnumDefinitionStub(parentStub, this, name);
  }

  @Override
  public void indexStub(@NotNull PbEnumDefinitionStub stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(ShortNameIndex.KEY, name);
    }
    QualifiedName qualifiedName = stub.getQualifiedName();
    if (qualifiedName != null) {
      sink.occurrence(QualifiedNameIndex.KEY, qualifiedName.toString());
    }
  }
}
