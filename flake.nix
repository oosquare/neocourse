{
  description = "Java Web Application Environment";

  inputs = {
    flake-parts.url = "github:hercules-ci/flake-parts";
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, flake-parts, ... }@inputs:
    flake-parts.lib.mkFlake { inherit inputs; } {
      systems = [ "x86_64-linux" ];

      perSystem = { self', inputs', system, ... }: let
        pkgs = import inputs.nixpkgs {
          inherit system;
        };
        mkShell = pkgs.mkShell.override { stdenv = pkgs.stdenvNoCC; };
      in {
        devShells.default = mkShell {
          packages = with pkgs; [
            jdk21
            nodejs
          ];
        };
      };
    };
}
