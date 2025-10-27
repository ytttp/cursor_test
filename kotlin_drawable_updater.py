#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Kotlin Drawable Reference Updater

This script processes an Excel file containing old_name, new_name, and color values,
then updates Kotlin files to replace drawable references according to specified rules.
"""

import pandas as pd
import re
import os
import glob
from pathlib import Path
from typing import List, Dict, Tuple, Optional

class KotlinDrawableUpdater:
    def __init__(self, excel_path: str, project_root: str = "."):
        self.excel_path = excel_path
        self.project_root = project_root
        self.mapping_data = []
        self.updated_files = []
        
    def read_excel_data(self) -> bool:
        """Read data from Excel file and extract required columns."""
        try:
            # Read Excel file
            df = pd.read_excel(self.excel_path)
            
            # Extract required columns
            for _, row in df.iterrows():
                old_name = str(row.get('old_name', '')).strip()
                new_name = str(row.get('new_name', '')).strip()
                color_value = str(row.get('项目color值', '')).strip()
                
                # Remove file extensions
                if old_name and '.' in old_name:
                    old_name = old_name.rsplit('.', 1)[0]
                if new_name and '.' in new_name:
                    new_name = new_name.rsplit('.', 1)[0]
                
                if old_name and new_name and color_value:
                    self.mapping_data.append({
                        'old_name': old_name,
                        'new_name': new_name,
                        'color_value': color_value
                    })
            
            print(f"Successfully loaded {len(self.mapping_data)} mapping entries from Excel file.")
            return True
            
        except Exception as e:
            print(f"Error reading Excel file: {e}")
            return False
    
    def find_kotlin_files(self) -> List[str]:
        """Find all Kotlin files in the project."""
        kotlin_files = []
        for root, dirs, files in os.walk(self.project_root):
            for file in files:
                if file.endswith('.kt'):
                    kotlin_files.append(os.path.join(root, file))
        return kotlin_files
    
    def create_replacement_patterns(self) -> List[Dict]:
        """Create regex patterns for different replacement scenarios."""
        patterns = []
        
        for mapping in self.mapping_data:
            old_name = re.escape(mapping['old_name'])
            new_name = mapping['new_name']
            color_value = mapping['color_value']
            
            # Pattern 1: FtRes.getDrawable(RDrawable.old_name) or FtRes.getDrawable(R.drawable.old_name) or FtRes.getDrawable(cn.futu.res.skin.R.drawable.old_name)
            pattern1 = {
                'regex': rf'FtRes\.getDrawable\(\s*(RDrawable\.{old_name}|R\.drawable\.{old_name}|cn\.futu\.res\.skin\.R\.drawable\.{old_name})\s*\)',
                'replacement': f'FtRes.getDrawable( RDrawable.{new_name}, cn.futu.res.skin.R.color.{color_value})',
                'description': 'FtRes.getDrawable pattern'
            }
            
            # Pattern 2: ResProxy.getDrawable(RDrawable.old_name) or ResProxy.getDrawable(R.drawable.old_name) or ResProxy.getDrawable(cn.futu.res.skin.R.drawable.old_name)
            pattern2 = {
                'regex': rf'ResProxy\.getDrawable\(\s*(RDrawable\.{old_name}|R\.drawable\.{old_name}|cn\.futu\.res\.skin\.R\.drawable\.{old_name})\s*\)',
                'replacement': f'ResProxy.getDrawable( RDrawable.{new_name}, cn.futu.res.skin.R.color.{color_value})',
                'description': 'ResProxy.getDrawable pattern'
            }
            
            # Pattern 3: RDrawable.old_name.toDrawable() or R.drawable.old_name.toDrawable() or cn.futu.res.skin.R.drawable.old_name.toDrawable()
            pattern3 = {
                'regex': rf'(RDrawable\.{old_name}|R\.drawable\.{old_name}|cn\.futu\.res\.skin\.R\.drawable\.{old_name})\.toDrawable\(\)',
                'replacement': f'FtRes.getDrawable(\\1, cn.futu.res.skin.R.color.{color_value})',
                'description': 'toDrawable() pattern'
            }
            
            patterns.extend([pattern1, pattern2, pattern3])
        
        return patterns
    
    def check_import_statement(self, content: str) -> bool:
        """Check if FtRes import statement exists."""
        return 'import cn.futu.component.FtRes' in content
    
    def add_import_statement(self, content: str) -> str:
        """Add FtRes import statement if not present."""
        if self.check_import_statement(content):
            return content
        
        # Find the last import statement
        import_lines = []
        lines = content.split('\n')
        last_import_index = -1
        
        for i, line in enumerate(lines):
            if line.strip().startswith('import '):
                last_import_index = i
                import_lines.append(line)
        
        # Add the new import
        new_import = 'import cn.futu.component.FtRes'
        if new_import not in import_lines:
            if last_import_index >= 0:
                lines.insert(last_import_index + 1, new_import)
            else:
                # No imports found, add at the beginning after package declaration
                package_index = -1
                for i, line in enumerate(lines):
                    if line.strip().startswith('package '):
                        package_index = i
                        break
                
                if package_index >= 0:
                    lines.insert(package_index + 1, new_import)
                else:
                    lines.insert(0, new_import)
        
        return '\n'.join(lines)
    
    def update_kotlin_file(self, file_path: str) -> bool:
        """Update a single Kotlin file with the replacement patterns."""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            original_content = content
            patterns = self.create_replacement_patterns()
            has_changes = False
            needs_ftres_import = False
            
            # Apply all patterns
            for pattern in patterns:
                matches = re.findall(pattern['regex'], content)
                if matches:
                    print(f"Found {len(matches)} matches for {pattern['description']} in {file_path}")
                    content = re.sub(pattern['regex'], pattern['replacement'], content)
                    has_changes = True
                    
                    # Check if we need to add FtRes import
                    if 'FtRes.getDrawable' in pattern['replacement']:
                        needs_ftres_import = True
            
            # Add import statement if needed
            if needs_ftres_import and not self.check_import_statement(content):
                content = self.add_import_statement(content)
                has_changes = True
                print(f"Added FtRes import to {file_path}")
            
            # Write back if changes were made
            if has_changes:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                self.updated_files.append(file_path)
                print(f"Successfully updated {file_path}")
                return True
            else:
                print(f"No changes needed in {file_path}")
                return False
                
        except Exception as e:
            print(f"Error updating file {file_path}: {e}")
            return False
    
    def process_all_files(self) -> bool:
        """Process all Kotlin files in the project."""
        if not self.mapping_data:
            print("No mapping data available. Please load Excel file first.")
            return False
        
        kotlin_files = self.find_kotlin_files()
        if not kotlin_files:
            print("No Kotlin files found in the project.")
            return False
        
        print(f"Found {len(kotlin_files)} Kotlin files to process.")
        
        success_count = 0
        for file_path in kotlin_files:
            if self.update_kotlin_file(file_path):
                success_count += 1
        
        print(f"Successfully updated {success_count} out of {len(kotlin_files)} files.")
        return success_count > 0
    
    def run(self) -> bool:
        """Main execution method."""
        print("Starting Kotlin Drawable Reference Updater...")
        
        # Step 1: Read Excel data
        if not self.read_excel_data():
            return False
        
        # Step 2: Process Kotlin files
        if not self.process_all_files():
            return False
        
        print(f"Processing completed. Updated {len(self.updated_files)} files:")
        for file_path in self.updated_files:
            print(f"  - {file_path}")
        
        return True

def main():
    """Main function to run the updater."""
    # Configuration
    excel_path = r"D:\kotlin_add_content.xlsx"  # Update this path as needed
    project_root = "."  # Current directory, update as needed
    
    # Check if Excel file exists
    if not os.path.exists(excel_path):
        print(f"Excel file not found at: {excel_path}")
        print("Please ensure the Excel file exists and update the path in the script.")
        return
    
    # Create and run updater
    updater = KotlinDrawableUpdater(excel_path, project_root)
    success = updater.run()
    
    if success:
        print("All operations completed successfully!")
    else:
        print("Some operations failed. Please check the error messages above.")

if __name__ == "__main__":
    main()