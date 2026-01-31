import argparse
import pathlib


def search_folder(path, extension, file_size=None):
    folder = pathlib.Path(path)
    files = list(folder.rglob(f'*.{extension}'))

    if not files:
        print(f'没有找到 {extension=} 扩展的文件')
        return

    if file_size is not None:
        files = [f for f in files
                 if f.stat().st_size > file_size]

    print(f'{len(files)} *.{extension} 个文件找到:')
    for file_path in files:
        print(file_path)


def main():
    parser = argparse.ArgumentParser(
        'PySearch',
        description='PySearch - 文件搜索器')
    parser.add_argument('-p', '--path',
                        help='搜索文件的根路径',
                        required=True,
                        dest='path')
    parser.add_argument('-e', '--ext',
                        help='文件扩展名',
                        required=True,
                        dest='extension')
    parser.add_argument('-s', '--size',
                        help='文件过滤大小',
                        type=int,
                        dest='size',
                        default=None)

    args = parser.parse_args()
    search_folder(args.path, args.extension, args.size)


if __name__ == '__main__':
    main()
