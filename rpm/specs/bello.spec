Name:           bello
Version:        0.1
Release:        1%{?dist}
Summary:        hello world example implemented in bash script

License:        MIT
URL:            https://example.com/%{name}
Source0:        https://example.com/%{name}/releases/%{name}-%{version}.tar.gz

Requires:       bash
BuildArch:      noarch

%description
the long-tail description for out hello world example implemented in
bash script.

%prep
%autosetup

%build

%install
mkdir -p %{buildroot}%{_bindir}
install -m 0755 %{name} %{buildroot}%{_bindir}/%{name}

%files
%license LICENSE
%{_bindir}/%{name}

%changelog
* Fri Sep  8 2023 yy
- first bello package
