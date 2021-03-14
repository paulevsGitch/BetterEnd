#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	fragData.emissivity = (fragData.spriteColor.b > 0.8) ? 1.0 : 0;
}
