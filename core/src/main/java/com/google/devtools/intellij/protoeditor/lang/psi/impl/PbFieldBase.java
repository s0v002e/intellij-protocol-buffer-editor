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
package com.google.devtools.intellij.protoeditor.lang.psi.impl;

import com.google.devtools.intellij.protoeditor.ide.util.PbIcons;
import com.google.devtools.intellij.protoeditor.lang.descriptor.Descriptor;
import com.google.devtools.intellij.protoeditor.lang.descriptor.DescriptorOptionType;
import com.google.devtools.intellij.protoeditor.lang.psi.PbExtendDefinition;
import com.google.devtools.intellij.protoeditor.lang.psi.PbField;
import com.google.devtools.intellij.protoeditor.lang.psi.PbFieldLabel;
import com.google.devtools.intellij.protoeditor.lang.psi.PbOneofDefinition;
import com.google.devtools.intellij.protoeditor.lang.psi.PbOptionExpression;
import com.google.devtools.intellij.protoeditor.lang.psi.PbOptionList;
import com.google.devtools.intellij.protoeditor.lang.psi.PbTypeName;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.QualifiedName;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class PbFieldBase extends PbNamedElementBase implements PbField {

  PbFieldBase(ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public String getName() {
    PsiElement id = getNameIdentifier();
    if (id != null) {
      return id.getText();
    }
    return null;
  }

  @NotNull
  @Override
  public List<PbOptionExpression> getOptions() {
    PbOptionList optionList = getOptionList();
    if (optionList == null) {
      return Collections.emptyList();
    }
    return optionList.getOptions();
  }

  @Nullable
  @Override
  public PbTypeName getExtendee() {
    PsiElement extendBody = getParent();
    if (extendBody == null) {
      return null;
    }
    PsiElement extendDefinition = extendBody.getParent();
    if (!(extendDefinition instanceof PbExtendDefinition)) {
      return null;
    }
    return ((PbExtendDefinition) extendDefinition).getTypeName();
  }

  @Nullable
  @Override
  public PbOneofDefinition getOneof() {
    PsiElement oneofBody = getParent();
    if (oneofBody == null) {
      return null;
    }
    PsiElement oneofDefinition = oneofBody.getParent();
    if (!(oneofDefinition instanceof PbOneofDefinition)) {
      return null;
    }
    return (PbOneofDefinition) oneofDefinition;
  }

  @NotNull
  @Override
  public QualifiedName getDescriptorOptionsTypeName(Descriptor descriptor) {
    return DescriptorOptionType.FIELD_OPTIONS.forDescriptor(descriptor);
  }

  @Override
  @Nullable
  public QualifiedName getExtensionOptionScope() {
    QualifiedName name = getQualifiedName();
    return name != null ? name.removeLastComponent() : null;
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PbIcons.FIELD;
  }

  @Override
  public CanonicalFieldLabel getCanonicalLabel() {
    PbFieldLabel declaredLabel = getDeclaredLabel();
    if (declaredLabel != null && "repeated".equals(declaredLabel.getText())) {
      return CanonicalFieldLabel.REPEATED;
    } else if (declaredLabel != null && "required".equals(declaredLabel.getText())) {
      return CanonicalFieldLabel.REQUIRED;
    }
    return CanonicalFieldLabel.OPTIONAL;
  }

  @Nullable
  abstract PbOptionList getOptionList();
}
