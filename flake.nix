{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/release-23.11";
    nixpkgs-unstable.url = "github:NixOS/nixpkgs/master";
  };

  outputs = { self, nixpkgs, nixpkgs-unstable }:
    let
      supportedSystems = [ "x86_64-linux" "x86_64-darwin" "aarch64-linux" "aarch64-darwin" ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = import nixpkgs {
            inherit system;
            config = { allowUnfree = true; };
          };
          pkgs-unstable = import nixpkgs-unstable {
            inherit system;
            config = { allowUnfree = true; };
          };
        in
        {
          default = pkgs.mkShell {
            packages = [
              pkgs-unstable.maven
              pkgs-unstable.jetbrains.idea-community
              pkgs.jdk17
            ];
            shellHook = ''
              rm -f symlinks/jdk-17.jdk
              export JAVA_HOME=${pkgs.jdk17}
              PATH="${pkgs.jdk17}/bin:$PATH"
              ln -sf ${pkgs.jdk17} symlinks/jdk-17.jdk
            '';
          };
        });
    };
}
