#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	fragData.emissivity = (fragData.spriteColor.r < 0.6 && (fragData.spriteColor.b > 0.3 || fragData.spriteColor.g > 0.3)) ? 1.0 : 0;
}
