
/**
 * Copyright (C) 2014 Lukasz Klich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.eclim.plugin.jdt.command.type;

import java.util.List;

import org.eclim.annotation.Command;

import org.eclim.command.CommandLine;
import org.eclim.command.Options;

import org.eclim.plugin.core.command.AbstractCommand;

import org.eclim.plugin.jdt.util.JavaUtils;

import org.eclim.util.file.Position;

import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.jdt.core.ICodeAssist;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;

/**
 * Command to handle java search requests.
 *
 * @author Lukasz Klich
 */
@Command(
  name = "java_method_info",
  options =
    "OPTIONAL n project ARG," +
    "OPTIONAL f file ARG," +
    "OPTIONAL o offset ARG," +
    "OPTIONAL e encoding ARG," +
    "OPTIONAL l length ARG," +
    "OPTIONAL p pattern ARG," +
    "OPTIONAL t type ARG," +
    "OPTIONAL x context ARG," +
    "OPTIONAL s scope ARG," +
    "OPTIONAL i case_insensitive NOARG"
)
public class MethodInfoCommand
  extends AbstractCommand
{

  @Override
  public Object execute(CommandLine commandLine)
    throws Exception
  {

      String project = commandLine.getValue(Options.NAME_OPTION);
      String scope = commandLine.getValue(Options.SCOPE_OPTION);
      String file = commandLine.getValue(Options.FILE_OPTION);
      String offset = commandLine.getValue(Options.OFFSET_OPTION);
      String length = commandLine.getValue(Options.LENGTH_OPTION);

      IJavaProject javaProject = JavaUtils.getJavaProject(project);

      int charOffset = getOffset(commandLine);
      IJavaElement currentElement = getElement(javaProject,
              file,
              charOffset,
              Integer.parseInt(length));
    // Assemble the final results in the sorted order
    // List<Position> results = null;
    // for (String sortKey : sortKeys) {
    //   List<Position> positions = positionMap.get(sortKey);
    //   if (positions == null) {
    //     continue;
    //   }

    //   if (results == null) {
    //     results = positions;
    //   } else {
    //     results.addAll(positions);
    //   }
    // }

      return currentElement.getElementName();
  }

  /**
   * Gets a IJavaElement by its position.
   *
   * @param javaProject The IJavaProject the file is in.
   * @param filename The file containing the element.
   * @param offset The offset of the element in the file.
   * @param length The lenght of the element.
   * @return The element.
   */
  protected IJavaElement getElement(
      IJavaProject javaProject, String filename, int offset, int length)
    throws Exception
  {
    ICodeAssist code = JavaUtils.getCompilationUnit(javaProject, filename);

    if (code != null){

      IJavaElement[] elements = code.codeSelect(offset, length);
      if(elements != null && elements.length > 0){
        return elements[0];
      } else {
          throw new RuntimeException("elements is null");
      }
    } else {
        throw new RuntimeException("code is null");
    }
  }
}
