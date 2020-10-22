#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	float p = (fragData.spriteColor.r + fragData.spriteColor.b) * 0.5;
	float d = abs(p - fragData.spriteColor.g);
	fragData.emissivity = (d > 0.3 && p > 0.5) ? 1 : 0;
}
