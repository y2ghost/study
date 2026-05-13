Name:           cello
Version:        1.0
Release:        1%{?dist}
Summary:        hello world example implemented in c

License:        MIT
URL:            https://example.com/%{name}
Source0:        https://example.com/%{name}/releases/%{name}-%{version}.tar.gz
Patch0:         cello-output-first-patch.patch

BuildRequires:  gcc
BuildRequires:  make

%description
the long-tail description for out hello world example implemented in
c.

%prep
%autosetup

%patch0

%build
make %{?_smp_mflags}

%install
%make_install

%files
%license LICENSE
%{_bindir}/%{name}

%changelog
* Fri Sep  8 2023 yy
- first cello package
