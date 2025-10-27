# Kotlin Drawable Reference Updater

This script processes an Excel file containing drawable name mappings and updates Kotlin files accordingly.

## Features

- Reads Excel file with `old_name`, `new_name`, and `项目color值` columns
- Removes file extensions from names automatically
- Updates multiple drawable reference patterns:
  - `FtRes.getDrawable(RDrawable.old_name)` → `FtRes.getDrawable( RDrawable.new_name, cn.futu.res.skin.R.color.color_value)`
  - `ResProxy.getDrawable(RDrawable.old_name)` → `ResProxy.getDrawable( RDrawable.new_name, cn.futu.res.skin.R.color.color_value)`
  - `RDrawable.old_name.toDrawable()` → `FtRes.getDrawable(RDrawable.new_name, cn.futu.res.skin.R.color.color_value)`
- Automatically adds missing import statements for `FtRes`
- Supports various drawable reference formats:
  - `RDrawable.old_name`
  - `R.drawable.old_name`
  - `cn.futu.res.skin.R.drawable.old_name`

## Requirements

- Python 3.6+
- pandas
- openpyxl

## Installation

```bash
pip install -r requirements.txt
```

## Usage

1. Place your Excel file at `D:\kotlin_add_content.xlsx` (or update the path in the script)
2. Ensure your Kotlin files are in the project directory
3. Run the script:

```bash
python kotlin_drawable_updater.py
```

## Excel File Format

The Excel file should contain the following columns:
- `old_name`: Original drawable name (with or without extension)
- `new_name`: New drawable name (with or without extension)
- `项目color值`: Color value to be used in the replacement

## Example

If your Excel contains:
| old_name | new_name | 项目color值 |
|----------|----------|-------------|
| icon_arrow_down_20_h3.webp | icon_arrow_up_20_h3.webp | primary_color |

The script will replace:
- `FtRes.getDrawable(RDrawable.icon_arrow_down_20_h3)` → `FtRes.getDrawable( RDrawable.icon_arrow_up_20_h3, cn.futu.res.skin.R.color.primary_color)`
- `RDrawable.icon_arrow_down_20_h3.toDrawable()` → `FtRes.getDrawable(RDrawable.icon_arrow_up_20_h3, cn.futu.res.skin.R.color.primary_color)`

## Notes

- The script automatically removes file extensions from names
- Only processes `.kt` files
- Preserves original file encoding (UTF-8)
- Adds import statements only when needed
- Provides detailed logging of all changes made