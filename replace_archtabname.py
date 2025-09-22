#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
脚本功能：
1. 遍历指定文件夹中的 .kt 和 .java 后缀的文件
2. 将符合条件的文件中 ArchTabName 替换为 AppletName
"""

import os
import re
import argparse
from pathlib import Path
from typing import List, Tuple


def find_files(directory: str, extensions: List[str]) -> List[Path]:
    """
    在指定目录中查找指定扩展名的文件
    
    Args:
        directory: 要搜索的目录路径
        extensions: 文件扩展名列表，如 ['.kt', '.java']
    
    Returns:
        找到的文件路径列表
    """
    files = []
    directory_path = Path(directory)
    
    if not directory_path.exists():
        print(f"错误：目录 '{directory}' 不存在")
        return files
    
    if not directory_path.is_dir():
        print(f"错误：'{directory}' 不是一个目录")
        return files
    
    for ext in extensions:
        pattern = f"**/*{ext}"
        files.extend(directory_path.glob(pattern))
    
    return files


def replace_in_file(file_path: Path, old_text: str, new_text: str) -> Tuple[bool, int]:
    """
    在文件中替换指定文本
    
    Args:
        file_path: 文件路径
        old_text: 要替换的旧文本
        new_text: 替换后的新文本
    
    Returns:
        (是否成功, 替换次数)
    """
    try:
        # 读取文件内容
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        # 计算替换次数
        replace_count = content.count(old_text)
        
        if replace_count == 0:
            return True, 0
        
        # 执行替换
        new_content = content.replace(old_text, new_text)
        
        # 写回文件
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        
        return True, replace_count
        
    except Exception as e:
        print(f"处理文件 {file_path} 时出错: {e}")
        return False, 0


def main():
    parser = argparse.ArgumentParser(description='在.kt和.java文件中将ArchTabName替换为AppletName')
    parser.add_argument('directory', help='要处理的目录路径')
    parser.add_argument('--old-text', default='ArchTabName', help='要替换的旧文本 (默认: ArchTabName)')
    parser.add_argument('--new-text', default='AppletName', help='替换后的新文本 (默认: AppletName)')
    parser.add_argument('--dry-run', action='store_true', help='预览模式，不实际修改文件')
    parser.add_argument('--verbose', '-v', action='store_true', help='显示详细信息')
    
    args = parser.parse_args()
    
    # 要处理的文件扩展名
    extensions = ['.kt', '.java']
    
    print(f"正在搜索目录: {args.directory}")
    print(f"文件类型: {', '.join(extensions)}")
    print(f"替换规则: '{args.old_text}' -> '{args.new_text}'")
    print(f"模式: {'预览模式' if args.dry_run else '实际修改'}")
    print("-" * 50)
    
    # 查找文件
    files = find_files(args.directory, extensions)
    
    if not files:
        print("未找到符合条件的文件")
        return
    
    print(f"找到 {len(files)} 个文件")
    
    total_replacements = 0
    processed_files = 0
    modified_files = 0
    
    for file_path in files:
        if args.verbose:
            print(f"处理文件: {file_path}")
        
        # 检查文件是否包含要替换的文本
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()
            
            if args.old_text not in content:
                if args.verbose:
                    print(f"  跳过: 文件中不包含 '{args.old_text}'")
                continue
                
        except Exception as e:
            print(f"  错误: 无法读取文件 {file_path}: {e}")
            continue
        
        # 计算替换次数
        replace_count = content.count(args.old_text)
        
        if args.dry_run:
            print(f"  [预览] {file_path}: 将替换 {replace_count} 次")
        else:
            # 执行替换
            success, actual_count = replace_in_file(file_path, args.old_text, args.new_text)
            if success:
                print(f"  [修改] {file_path}: 已替换 {actual_count} 次")
                modified_files += 1
            else:
                print(f"  [失败] {file_path}: 替换失败")
                continue
        
        total_replacements += replace_count
        processed_files += 1
    
    print("-" * 50)
    print(f"处理完成:")
    print(f"  处理文件数: {processed_files}")
    print(f"  修改文件数: {modified_files}")
    print(f"  总替换次数: {total_replacements}")


if __name__ == "__main__":
    main()