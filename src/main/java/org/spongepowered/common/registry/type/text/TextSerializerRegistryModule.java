/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry.type.text;

import static org.spongepowered.common.text.SpongeTexts.COLOR_CHAR;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.registry.RegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.common.text.serializer.JsonTextSerializer;
import org.spongepowered.common.text.serializer.PlainTextSerializer;
import org.spongepowered.common.text.serializer.SpongeFormattingCodeTextSerializer;
import org.spongepowered.common.text.serializer.xml.TextXmlTextSerializer;

public final class TextSerializerRegistryModule implements RegistryModule {

    @RegisterCatalog(TextSerializers.class)
    private static final ImmutableMap<String, TextSerializer> textSerializerMappings = ImmutableMap.<String, TextSerializer>builder()
            .put("plain", new PlainTextSerializer())
            .put("legacy_formatting_code", new SpongeFormattingCodeTextSerializer(COLOR_CHAR))
            .put("formatting_code", new SpongeFormattingCodeTextSerializer('&'))
            .put("json", new JsonTextSerializer())
            .put("text_xml", new TextXmlTextSerializer())
            .build();

}
