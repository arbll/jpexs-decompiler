/*
 *  Copyright (C) 2010-2011 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.asdec.tags;

import com.jpexs.asdec.SWFInputStream;
import com.jpexs.asdec.SWFOutputStream;
import com.jpexs.asdec.tags.base.CharacterTag;
import com.jpexs.asdec.types.MATRIX;
import com.jpexs.asdec.types.RECT;
import com.jpexs.asdec.types.TEXTRECORD;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author JPEXS
 */
public class DefineText2Tag extends CharacterTag  {

   public int characterID;
   public RECT textBounds;
   public MATRIX textMatrix;
   public int glyphBits;
   public int advanceBits;
   public List<TEXTRECORD> textRecords;

   @Override
   public int getCharacterID() {
      return characterID;
   }
   
   /**
    * Gets data bytes
    *
    * @param version SWF version
    * @return Bytes of data
    */
   @Override
   public byte[] getData(int version) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStream os = baos;
      SWFOutputStream sos = new SWFOutputStream(os, version);
      try {
         sos.writeUI16(characterID);
         sos.writeRECT(textBounds);
         sos.writeMatrix(textMatrix);
         sos.writeUI8(glyphBits);
         sos.writeUI8(advanceBits);
         for (TEXTRECORD tr : textRecords) {
            sos.writeTEXTRECORD(tr, true, glyphBits, advanceBits);
         }
         sos.writeUI8(0);
      } catch (IOException e) {
      }
      return baos.toByteArray();
   }

   /**
    * Constructor
    *
    * @param data Data bytes
    * @param version SWF version
    * @throws IOException
    */
   public DefineText2Tag(byte data[], int version, long pos) throws IOException {
      super(33,"DefineText2", data, pos);
      SWFInputStream sis = new SWFInputStream(new ByteArrayInputStream(data), version);
      characterID = sis.readUI16();
      textBounds = sis.readRECT();
      textMatrix = sis.readMatrix();
      glyphBits = sis.readUI8();
      advanceBits = sis.readUI8();
      textRecords = new ArrayList<TEXTRECORD>();
      TEXTRECORD tr;
      while ((tr = sis.readTEXTRECORD(true, glyphBits, advanceBits)) != null) {
         textRecords.add(tr);
      }
   }

}
